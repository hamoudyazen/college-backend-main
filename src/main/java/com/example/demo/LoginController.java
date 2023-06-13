package com.example.demo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.Authentication;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.internal.FirebaseCustomAuthToken;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.internal.FirebaseService;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.google.api.core.ApiFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {

    @Autowired
    private EmailSenderService emailSenderService;


    @GetMapping("/courses")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Course>> courses() {
        try {
            ApiFuture<QuerySnapshot> future = FirestoreClient.getFirestore().collection("courses").get();
            List<Course> courseList = new ArrayList<>();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                Course course = document.toObject(Course.class);
                courseList.add(course);
            }
            return new ResponseEntity<>(courseList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/forgot-password")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
            String verificationLink = FirebaseAuth.getInstance().generatePasswordResetLink(email);
            String subject = "Reset Your Password";
            String body = "Please click on the following link to reset your password: " + verificationLink;
            emailSenderService.sendEmail(email, subject, body);
            return ResponseEntity.ok().body("{\"message\": \"password-rest email link generated successfully.\", \"password-rest-link\": \"" + verificationLink + "\"}");
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error generating password-rest email link"));
        }
    }


    @PostMapping("/email-verification")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> emailVerification(@RequestParam String email) {
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
            String verificationLink = FirebaseAuth.getInstance().generateEmailVerificationLink(email);
            String subject = "Verify Your Email";
            String body = "Please click on the following link to Verify your Email: " + verificationLink;
            emailSenderService.sendEmail(email, subject, body);
            return ResponseEntity.ok().body("{\"message\": \"Verification email link generated successfully.\", \"verificationLink\": \"" + verificationLink + "\"}");
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error generating verification email link"));
        }
    }


    @GetMapping("/getName")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Map<String, String>> getName(@RequestParam String email) {
        try {
            ApiFuture<QuerySnapshot> future = FirestoreClient.getFirestore().collection("users").whereEqualTo("email", email).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            if (documents.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "User not found"));
            } else if (documents.size() > 1) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Multiple users found"));
            } else {
                User user = documents.get(0).toObject(User.class);
                Map<String, String> responseMap = new HashMap<>();
                responseMap.put("firstname", user.getFirstname());
                responseMap.put("lastname", user.getLastname()); // Add the lastname to the response map
                return ResponseEntity.ok().body(responseMap);
            }
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Error getting user record"));
        }
    }


    @GetMapping("/getRole")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Map<String, String>> getRole(String email) {
        try {
            CollectionReference usersRef = FirestoreClient.getFirestore().collection("users");
            Query query = usersRef.whereEqualTo("email", email);
            ApiFuture<QuerySnapshot> querySnapshot = query.get();

            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                Map<String, Object> userData = document.getData();
                String role = userData.get("role").toString();
                return ResponseEntity.ok().body(Collections.singletonMap("role", role));
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "User not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Error getting user record"));
        }
    }


    @GetMapping("/getId")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Map<String, String>> getId(@RequestParam String email) {
        try {
            ApiFuture<QuerySnapshot> future = FirestoreClient.getFirestore().collection("users").whereEqualTo("email", email).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            if (documents.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "Id not found"));
            } else if (documents.size() > 1) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Multiple ID'S found"));
            } else {
                String id = documents.get(0).getString("id"); // Get the value of "id" from the document
                Map<String, String> responseMap = new HashMap<>();
                responseMap.put("id", id);
                return ResponseEntity.ok().body(responseMap);
            }
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Error getting user record"));
        }
    }


    @GetMapping("/getTeacherCourses")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Course>> getTeacherCourses(@RequestParam String id) {
        try {
            ApiFuture<QuerySnapshot> future = FirestoreClient.getFirestore().collection("courses").whereEqualTo("teacherID", id).get();
            List<Course> courseList = new ArrayList<>();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                Course course = document.toObject(Course.class);
                courseList.add(course);
            }
            return new ResponseEntity<>(courseList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getUserDetails")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<User>> getUserDetails(@RequestParam String email) {
        try {
            ApiFuture<QuerySnapshot> future = FirestoreClient.getFirestore().collection("users").whereEqualTo("email", email).get();
            List<User> userList = new ArrayList<>();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                User user = document.toObject(User.class);
                userList.add(user);
            }
            return ResponseEntity.ok(userList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/registercourse")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> registerCourse(@RequestBody Course course) {
        try {
            String name = course.getName();

            // Query Firestore to get a course with the given name
            QuerySnapshot querySnapshot = FirestoreClient.getFirestore().collection("courses")
                    .whereEqualTo("name", name).get().get();
            if (!querySnapshot.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Course already exists"));
            }

            // Generate a unique ID for the course
            String id = UUID.randomUUID().toString();
            course.setId(id);
            course.setStudentsArray(Collections.emptyList()); // empty list of student IDs

            // Query Firestore to get the teacher by their ID
            String teacherID = course.getTeacherID();
            QuerySnapshot teacherQuerySnapshot = FirestoreClient.getFirestore().collection("users")
                    .whereEqualTo("id", teacherID).get().get();
            List<User> teacherList = teacherQuerySnapshot.toObjects(User.class);
            if (teacherList.size() >= 1) {
                User teacher = teacherList.get(0);
                List<String> courseIds = teacher.getCourseIds();
                if (courseIds.size() >= 5) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ErrorResponse("Teacher has reached the maximum number of courses"));
                }
                courseIds.add(id);
                teacher.setCourseIds(courseIds);
                FirestoreClient.getFirestore().collection("users").document(teacher.getId()).set(teacher);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Teacher not found"));
            }

            // Add the course to Firestore
            FirestoreClient.getFirestore().collection("courses").document(id).set(course);

            // Update the coursesList field in the major's collection
            String majorName = course.getMajor();
            QuerySnapshot majorQuerySnapshot = FirestoreClient.getFirestore().collection("majors")
                    .whereEqualTo("majorName", majorName).get().get();
            List<Major> majorList = majorQuerySnapshot.toObjects(Major.class);
            for (Major major : majorList) {
                List<String> coursesList = major.getCoursesList();
                coursesList.add(id);
                major.setCoursesList(coursesList);
                FirestoreClient.getFirestore().collection("majors").document(major.getId()).set(major);
            }

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred during registration"));
        }
    }


    @PostMapping("/register")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            String email = user.getEmail();

            // Query Firestore to get a user with the given email
            QuerySnapshot querySnapshot = FirestoreClient.getFirestore().collection("users")
                    .whereEqualTo("email", email).get().get();
            if (!querySnapshot.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User Already Exist"));
            }
            if (user.getPassword().length() < 8) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Password Short"));
            }

            String id = UUID.randomUUID().toString();
            user.setId(id);
            String hashedPassword = hashPassword(user.getPassword());
            user.setPassword(hashedPassword);
            user.setRole("student");
            user.setEditingSchedule(false);
            user.setCourseIds(Collections.emptyList()); // empty list of course IDs
            user.setImage("https://firebasestorage.googleapis.com/v0/b/collegeproject-b85f0.appspot.com/o/profile%2Fdefault.jpg?alt=media&token=3ddf9690-9677-4a63-9a45-5a977d751ef0"); // empty list of course IDs


            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(user.getEmail())
                    .setDisplayName(user.getFirstname())
                    .setUid(id);
            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
            FirestoreClient.getFirestore().collection("users").document(id).set(user);

            String verificationLink = FirebaseAuth.getInstance().generateEmailVerificationLink(user.getEmail());
            String subject = "Verify Your Email";
            String body = "Please click on the following link to Verify your Email: " + verificationLink;
            emailSenderService.sendEmail(user.getEmail(), subject, body);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Registration failed"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("An error occurred during registration"));
        }
    }

    @DeleteMapping("/deleteEmail")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> deleteEmail(@RequestParam String courseId, @RequestParam String email) {
        try {
            // Query Firestore to get the course with the given ID
            DocumentReference courseRef = FirestoreClient.getFirestore().collection("courses").document(courseId);
            ApiFuture<DocumentSnapshot> courseFuture = courseRef.get();
            DocumentSnapshot courseDoc = courseFuture.get();
            if (!courseDoc.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Course not found"));
            }

            // Delete the student's email from the course's studentsArray field
            Course course = courseDoc.toObject(Course.class);
            List<String> students = course.getStudentsArray();
            boolean found = false;
            for (int i = 0; i < students.size(); i++) {
                if (students.get(i).equals(email)) {
                    students.remove(i);
                    found = true;
                    break;
                }
            }
            if (!found) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Student not found in course"));
            }
            course.setStudentsArray(students);
            ApiFuture<WriteResult> writeResultFuture = courseRef.set(course);
            writeResultFuture.get();

            return ResponseEntity.ok().body(Map.of("message", "Email deleted successfully."));
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error deleting email"));
        }
    }

    @PutMapping("/updateEmail")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> updateEmail(@RequestParam String courseId, @RequestParam String originalEmail, @RequestParam String updatedEmail) {
        try {
            // Query Firestore to get the course with the given ID
            DocumentReference courseRef = FirestoreClient.getFirestore().collection("courses").document(courseId);
            ApiFuture<DocumentSnapshot> courseFuture = courseRef.get();
            DocumentSnapshot courseDoc = courseFuture.get();
            if (!courseDoc.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Course not found"));
            }

            // Update the student's email in the course's studentsArray field
            Course course = courseDoc.toObject(Course.class);
            List<String> students = course.getStudentsArray();
            boolean found = false;
            for (int i = 0; i < students.size(); i++) {
                if (students.get(i).equals(originalEmail)) {
                    students.set(i, updatedEmail);
                    found = true;
                    break;
                }
            }
            if (!found) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Student not found in course"));
            }
            course.setStudentsArray(students);
            ApiFuture<WriteResult> writeResultFuture = courseRef.set(course);
            writeResultFuture.get();

            return ResponseEntity.ok().body(Map.of("message", "Email updated successfully."));
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error updating email"));
        }
    }

    @PostMapping("/addStudent")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> addStudent(@RequestBody Map<String, String> request) {
        try {
            String courseId = request.get("courseId");
            String newEmail = request.get("newEmail");
            // Query Firestore to get the course with the given ID
            DocumentReference courseRef = FirestoreClient.getFirestore().collection("courses").document(courseId);
            ApiFuture<DocumentSnapshot> courseFuture = courseRef.get();
            DocumentSnapshot courseDoc = courseFuture.get();
            if (!courseDoc.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Course not found"));
            }

            // Check if student already exists in course
            Course course = courseDoc.toObject(Course.class);
            List<String> students = course.getStudentsArray();


            // Add new student to course
            students.add(newEmail);
            courseRef.update("studentsArray", students).get();


            return ResponseEntity.ok().body(Map.of("message", "Student added successfully."));
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error adding student"));
        }
    }

    public boolean isEmailVerified(String email) {
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
            return userRecord.isEmailVerified();
        } catch (FirebaseAuthException e) {
            // Handle exception
            return false;
        }
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            String email = loginRequest.getEmail();

            // Query Firestore to get a user with the given email
            ApiFuture<QuerySnapshot> future = FirestoreClient.getFirestore().collection("users")
                    .whereEqualTo("email", email).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            if (documents.isEmpty()) {
                System.out.println("User record is null for email: " + email);
                return ResponseEntity.ok(0);
            }

            // Check if any user has the same password as provided
            for (QueryDocumentSnapshot document : documents) {
                User user = document.toObject(User.class);
                if (new BCryptPasswordEncoder().matches(loginRequest.getPassword(), user.getPassword())) {
                    if (isEmailVerified(email)) {
                        return ResponseEntity.ok(1);
                    } else {
                        return ResponseEntity.ok(0);
                    }

                } else {
                    return ResponseEntity.ok(0);
                }
            }

            // No user found with the provided email and password
            return ResponseEntity.ok(0);

        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error getting user record: " + e.getMessage());
            return ResponseEntity.ok(0);
        }
    }

    private String hashPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }


    //profile//

    @PutMapping("/updateProfileEmail")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> updateProfileEmail(@RequestParam String oldEmail, @RequestParam String newEmail, @RequestParam String userId) {
        try {
            // Query Firestore to get a user with the given ID
            DocumentReference userRef = FirestoreClient.getFirestore().collection("users").document(userId);
            ApiFuture<DocumentSnapshot> userFuture = userRef.get();
            DocumentSnapshot userDoc = userFuture.get();

            if (!userDoc.exists()) {
                System.out.println("User record not found for ID: " + userId);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User Not Registered"));
            }

            // Check if the old email matches the user's current email
            User user = userDoc.toObject(User.class);
            if (!user.getEmail().equals(oldEmail)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Old email does not match the user's current email"));
            }

            // Update the user's email in Firestore
            userRef.update("email", newEmail);

            return ResponseEntity.ok().build();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error getting user record: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Login failed"));
        }
    }


    @PutMapping("/updateProfileFirstname")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> updateProfileFirstname(@RequestParam String oldEmail, @RequestParam String newEmail, @RequestParam String userId) {
        try {
            // Query Firestore to get a user with the given ID
            DocumentReference userRef = FirestoreClient.getFirestore().collection("users").document(userId);
            ApiFuture<DocumentSnapshot> userFuture = userRef.get();
            DocumentSnapshot userDoc = userFuture.get();

            if (!userDoc.exists()) {
                System.out.println("User record not found for ID: " + userId);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User Not Registered"));
            }

            // Check if the old email matches the user's current email
            User user = userDoc.toObject(User.class);
            if (!user.getFirstname().equals(oldEmail)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Old email does not match the user's current email"));
            }

            // Update the user's email in Firestore
            userRef.update("firstname", newEmail);

            return ResponseEntity.ok().build();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error getting user record: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Login failed"));
        }
    }


    @PutMapping("/updateProfileLastname")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> updateProfileLastname(@RequestParam String oldEmail, @RequestParam String newEmail, @RequestParam String userId) {
        try {
            // Query Firestore to get a user with the given ID
            DocumentReference userRef = FirestoreClient.getFirestore().collection("users").document(userId);
            ApiFuture<DocumentSnapshot> userFuture = userRef.get();
            DocumentSnapshot userDoc = userFuture.get();

            if (!userDoc.exists()) {
                System.out.println("User record not found for ID: " + userId);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User Not Registered"));
            }

            // Check if the old email matches the user's current email
            User user = userDoc.toObject(User.class);
            if (!user.getLastname().equals(oldEmail)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Old email does not match the user's current email"));
            }

            // Update the user's email in Firestore
            userRef.update("lastname", newEmail);

            return ResponseEntity.ok().build();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error getting user record: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Login failed"));
        }
    }


    @PutMapping("/updateProfilePassword")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> updateProfilePassword(@RequestParam String oldEmail, @RequestParam String newEmail, @RequestParam String userId) {
        try {
            // Query Firestore to get a user with the given ID
            DocumentReference userRef = FirestoreClient.getFirestore().collection("users").document(userId);
            ApiFuture<DocumentSnapshot> userFuture = userRef.get();
            DocumentSnapshot userDoc = userFuture.get();

            if (!userDoc.exists()) {
                System.out.println("User record not found for ID: " + userId);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User Not Registered"));
            }


            // Check if the old email matches the user's current email
            User user = userDoc.toObject(User.class);

            String hashedPassword = hashPassword(newEmail);

            // Update the user's email in Firestore
            userRef.update("password", hashedPassword);

            return ResponseEntity.ok().build();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error getting user record: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Login failed"));
        }
    }

    @PutMapping("/updateProfilePicture")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> updateProfilePicture(@RequestParam String downloadURL, @RequestParam String userId) {
        try {
            // Query Firestore to get a user with the given ID
            DocumentReference userRef = FirestoreClient.getFirestore().collection("users").document(userId);
            ApiFuture<DocumentSnapshot> userFuture = userRef.get();
            DocumentSnapshot userDoc = userFuture.get();

            if (!userDoc.exists()) {
                System.out.println("User record not found for ID: " + userId);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User Not Registered"));
            }

            // Replace spaces with '%20' in the downloadURL
            String newDownloadURL = downloadURL.replaceAll(" ", "%20");

            // Update the user's image in Firestore
            userRef.update("image", newDownloadURL);

            return ResponseEntity.ok().build();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error getting user record: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Login failed"));
        }
    }


    @PutMapping("/isEmailExists")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Boolean> isEmailExists(@RequestParam String email) {
        try {
            // Query Firestore to get a user with the given email
            CollectionReference usersRef = FirestoreClient.getFirestore().collection("users");
            Query query = usersRef.whereEqualTo("email", email);
            ApiFuture<QuerySnapshot> queryFuture = query.get();
            QuerySnapshot querySnapshot = queryFuture.get();

            if (querySnapshot.isEmpty()) {
                System.out.println("User record not found for email: " + email);
                return ResponseEntity.ok(false);
            }

            return ResponseEntity.ok(true);
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error getting user record: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }


    @PutMapping("/isBirthdaySame")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Boolean> isBirthdaySame(@RequestParam String birthday) {
        try {
            // Query Firestore to get a user with the given email
            CollectionReference usersRef = FirestoreClient.getFirestore().collection("users");
            Query query = usersRef.whereEqualTo("birthday", birthday);
            ApiFuture<QuerySnapshot> queryFuture = query.get();
            QuerySnapshot querySnapshot = queryFuture.get();

            if (querySnapshot.isEmpty()) {
                System.out.println("User record not found for email: " + birthday);
                return ResponseEntity.ok(false);
            }

            return ResponseEntity.ok(true);
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error getting user record: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @PutMapping("/updatePassword")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> updatePassword(@RequestParam String newPassword, @RequestParam String email) {
        try {
            // Query Firestore to get a user with the given email
            CollectionReference usersRef = FirestoreClient.getFirestore().collection("users");
            Query query = usersRef.whereEqualTo("email", email);
            ApiFuture<QuerySnapshot> queryFuture = query.get();
            QuerySnapshot querySnapshot = queryFuture.get();

            if (querySnapshot.isEmpty()) {
                System.out.println("User record not found for email: " + email);
                return ResponseEntity.ok(false);
            }

            // Get the first user in the query results (should be the only one)
            DocumentSnapshot userDoc = querySnapshot.getDocuments().get(0);

            // Hash the new password
            String hashedPassword = hashPassword(newPassword);

            // Update the user's password in Firestore
            userDoc.getReference().update("password", hashedPassword);

            return ResponseEntity.ok().build();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error getting user record: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Login failed"));
        }
    }


    @PostMapping("/addAssignment")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> addAssignment(@RequestBody Assignment assignment) {
        try {
            String name = assignment.getName();

            // Query Firestore to get an assignment with the given name
            QuerySnapshot querySnapshot = FirestoreClient.getFirestore().collection("assignment")
                    .whereEqualTo("name", name).get().get();


            // Generate a unique ID for the assignmentx
            String id = UUID.randomUUID().toString();
            assignment.setAssignment_id(id);
            assignment.setStudentUploads(Collections.emptyList()); // empty list of student IDs


            // Add the assignment to Firestore
            FirestoreClient.getFirestore().collection("assignments").document(id).set(assignment);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("An error occurred during assignment creation"));
        }
    }

    @GetMapping("/ShowAvailableCoursesForStudent")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Course>> showAvailableCoursesForStudent() {
        try {
            // Query the "courses" collection for courses with student capacity less than 20
            ApiFuture<QuerySnapshot> future = FirestoreClient.getFirestore().collection("courses").get();

            List<Course> courseList = new ArrayList<>();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                Course course = document.toObject(Course.class);
                String[] studentsArray = course.getStudentsArray().toArray(new String[0]);
                if (studentsArray != null && studentsArray.length < 20) {
                    courseList.add(course);
                }
            }
            return new ResponseEntity<>(courseList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/RegisterForCourse")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Boolean> RegisterForCourse(@RequestParam String courseID, @RequestParam String email) {
        try {
            // Get the course document from the "courses" collection using the provided courseID
            DocumentReference courseRef = FirestoreClient.getFirestore().collection("courses").document(courseID);
            ApiFuture<DocumentSnapshot> courseFuture = courseRef.get();
            DocumentSnapshot courseSnapshot = courseFuture.get();
            if (courseSnapshot.exists()) {
                // If the course exists, update the studentsArray field with the new student email
                List<String> studentsArray = (List<String>) courseSnapshot.get("studentsArray");
                if (studentsArray == null) {
                    studentsArray = new ArrayList<>();
                }
                if (!studentsArray.contains(email)) {
                    studentsArray.add(email);
                    ApiFuture<WriteResult> updateFuture = courseRef.update("studentsArray", studentsArray);
                    updateFuture.get();
                    return new ResponseEntity<>(true, HttpStatus.OK);
                }
            }
            // If the course does not exist or the student is already registered, return false
            return new ResponseEntity<>(false, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/StudentCourses")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Course>> studentCourses(@RequestParam String email) {
        try {
            // Query the "courses" collection for courses that contain the student's email
            ApiFuture<QuerySnapshot> future = FirestoreClient.getFirestore()
                    .collection("courses")
                    .whereArrayContains("studentsArray", email)
                    .get();

            List<Course> courseList = new ArrayList<>();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                Course course = document.toObject(Course.class);
                courseList.add(course);
            }
            return new ResponseEntity<>(courseList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/StudentAssignments")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Assignment>> studentAssignments(@RequestParam String email) {
        try {
            List<String> courseIds = getStudentCourseIds(email);
            System.out.println("Course IDs for student " + email + ": " + courseIds.toString());

            // Query the "assignments" collection for assignments where the "course_id" field matches a course that the student is registered for
            Query query = FirestoreClient.getFirestore()
                    .collection("assignments")
                    .whereIn("course_id", courseIds);
            ApiFuture<QuerySnapshot> future = query.get();

            List<Assignment> assignmentList = new ArrayList<>();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                Assignment assignment = document.toObject(Assignment.class);
                System.out.println("Assignment found for student " + email + ": " + assignment.toString());
                assignmentList.add(assignment);
            }
            return new ResponseEntity<>(assignmentList, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error getting assignments for student " + email + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private List<String> getStudentCourseIds(String email) throws Exception {
        // Query the "courses" collection for courses that contain the student's email in the "studentsArray" field
        ApiFuture<QuerySnapshot> future = FirestoreClient.getFirestore()
                .collection("courses")
                .whereArrayContains("studentsArray", email)
                .get();

        List<String> courseIdList = new ArrayList<>();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            String courseId = document.getId();
            courseIdList.add(courseId);
        }
        return courseIdList;
    }


    @PostMapping("/submissions")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> submissions(@RequestBody Submission submission) {
        try {
            String assignmentId = submission.getSubmission_assignment_id();
            String studentId = submission.getSubmission_student_id();
            // Query Firestore to get an assignment with the given ID and student ID
            QuerySnapshot querySnapshot = FirestoreClient.getFirestore().collection("submissions")
                    .whereEqualTo("submission_assignment_id", assignmentId)
                    .whereEqualTo("submission_student_id", studentId)
                    .get()
                    .get();

            // Check if an assignment with the same ID and student ID already exists
            if (!querySnapshot.isEmpty()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("You have already uploaded a submission for this assignment"));
            }

            // Generate a unique ID for the submission
            String id = UUID.randomUUID().toString();
            submission.setSubmission_id(id);

            // Add the submission to Firestore
            FirestoreClient.getFirestore().collection("submissions").document(id).set(submission);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("An error occurred during submission creation"));
        }
    }


    @GetMapping("/teacherAssignmentsSubmissions")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Submission>> teacherAssignmentsSubmissions(@RequestParam String email) {
        try {
            List<String> courseIds = getTeacherCourseIds(email);
            System.out.println("Course IDs for teacher " + email + ": " + courseIds.toString());

            // Query the "assignments" collection for assignments where the "course_id" field matches a course that the teacher teaches
            Query assignmentsQuery = FirestoreClient.getFirestore()
                    .collection("assignments")
                    .whereIn("course_id", courseIds);
            ApiFuture<QuerySnapshot> assignmentsFuture = assignmentsQuery.get();

            List<Submission> submissionList = new ArrayList<>();
            List<QueryDocumentSnapshot> assignmentsDocuments = assignmentsFuture.get().getDocuments();
            for (QueryDocumentSnapshot assignmentsDocument : assignmentsDocuments) {
                String assignmentId = assignmentsDocument.getId();

                // Query the "submissions" collection for submissions where the "submission_assignment_id" field matches the assignment ID
                Query submissionsQuery = FirestoreClient.getFirestore()
                        .collection("submissions")
                        .whereEqualTo("submission_assignment_id", assignmentId);
                ApiFuture<QuerySnapshot> submissionsFuture = submissionsQuery.get();

                List<QueryDocumentSnapshot> submissionsDocuments = submissionsFuture.get().getDocuments();
                for (QueryDocumentSnapshot submissionsDocument : submissionsDocuments) {
                    Submission submission = submissionsDocument.toObject(Submission.class);
                    System.out.println("Submission found for assignment " + assignmentId + ": " + submission.toString());
                    submissionList.add(submission);
                }
            }
            return new ResponseEntity<>(submissionList, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error getting submissions for teacher " + email + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<String> getTeacherCourseIds(String teacherId) throws Exception {
        // Query the "courses" collection for courses that have the given teacher ID in the "teacherID" field
        ApiFuture<QuerySnapshot> future = FirestoreClient.getFirestore()
                .collection("courses")
                .whereEqualTo("teacherID", teacherId)
                .get();

        List<String> courseIdList = new ArrayList<>();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            String courseId = document.getId();
            courseIdList.add(courseId);
        }
        return courseIdList;
    }


    @GetMapping("/studentMySubmissions")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Submission>> studentMySubmissions(@RequestParam String id) {
        try {
            // Query the "submissions" collection for submissions where the "submissions_student_id" field matches the given ID
            Query submissionsQuery = FirestoreClient.getFirestore()
                    .collection("submissions")
                    .whereEqualTo("submission_student_id", id);
            ApiFuture<QuerySnapshot> submissionsFuture = submissionsQuery.get();

            // Convert each document to a Submission object and add it to the list
            List<Submission> submissionList = new ArrayList<>();
            for (DocumentSnapshot document : submissionsFuture.get().getDocuments()) {
                Submission submission = document.toObject(Submission.class);
                submissionList.add(submission);
            }

            return new ResponseEntity<>(submissionList, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error getting submissions for student " + id + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/studentDeleteSubmission")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> studentDeleteSubmission(@RequestParam String id) {
        try {
            // Delete all submissions with the given student ID
            WriteBatch batch = FirestoreClient.getFirestore().batch();
            Query submissionsQuery = FirestoreClient.getFirestore()
                    .collection("submissions")
                    .whereEqualTo("submission_student_id", id);
            ApiFuture<QuerySnapshot> submissionsFuture = submissionsQuery.get();
            for (DocumentSnapshot document : submissionsFuture.get().getDocuments()) {
                batch.delete(document.getReference());
            }
            batch.commit().get();

            return ResponseEntity.ok().body(Map.of("message", "Submission deleted successfully."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error deleting submission"));
        }
    }


    //////////

    @PostMapping("/CourseMaterial")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> CourseMaterial(@RequestBody CourseMaterial courseMaterial) {
        try {
            // Generate a unique ID for the assignmentx
            String id = UUID.randomUUID().toString();
            courseMaterial.setId(id);


            // Add the assignment to Firestore
            FirestoreClient.getFirestore().collection("CourseMaterial").document(id).set(courseMaterial);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("An error occurred during assignment creation"));
        }
    }

    @GetMapping("/showAllCourseMaterials")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<CourseMaterial>> showAllCourseMaterials() {
        try {
            // Query the "courses" collection for courses with student capacity less than 20
            ApiFuture<QuerySnapshot> future = FirestoreClient.getFirestore().collection("CourseMaterial").get();

            List<CourseMaterial> courseList = new ArrayList<>();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                CourseMaterial courseMaterial = document.toObject(CourseMaterial.class);
                courseList.add(courseMaterial);

            }
            return new ResponseEntity<>(courseList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static class LoginRequest {
        private String email;
        private String password;

        public LoginRequest() {
        }

        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }


    @PostMapping("/addSundaySchedule")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> addSundaySchedule(@RequestBody sundaySchedule sc) {
        try {
            String scheduleMajor = sc.getScheduleMajor();

            // Query Firestore to check if schedule already exists for the given major
            QuerySnapshot querySnapshot = FirestoreClient.getFirestore().collection("sundaySchedule")
                    .whereEqualTo("scheduleMajor", scheduleMajor).get().get();
            if (!querySnapshot.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Schedule already exists for the given major"));
            }

            String id = UUID.randomUUID().toString();
            sc.setId(id);

            FirestoreClient.getFirestore().collection("sundaySchedule").document(id).set(sc);
            System.out.println("good");
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            System.out.println("failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("An error occurred while adding Sunday schedule"));
        }
    }


    @GetMapping("/getMajorCourses")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<String>> getMajorCourses(@RequestParam String majorName) {
        try {
            Firestore firestore = FirestoreClient.getFirestore();
            Query query = firestore.collection("majors").whereEqualTo("majorName", majorName);
            ApiFuture<QuerySnapshot> future = query.get();

            List<String> majorCoursesArray = new ArrayList<>();
            QuerySnapshot querySnapshot = future.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                List<String> courseIds = (List<String>) document.get("coursesList");
                if (courseIds != null) {
                    majorCoursesArray.addAll(courseIds);
                }
            }

            return new ResponseEntity<>(majorCoursesArray, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/getMajorDetails")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Major>> getMajorDetails(@RequestParam String majorName) {
        try {
            Firestore firestore = FirestoreClient.getFirestore();
            CollectionReference collectionRef = firestore.collection("majors");
            Query query = collectionRef.whereEqualTo("majorName", majorName);
            ApiFuture<QuerySnapshot> future = query.get();
            List<Major> majorList = new ArrayList<>();
            QuerySnapshot querySnapshot = future.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                Major major = document.toObject(Major.class);
                majorList.add(major);
            }
            return ResponseEntity.ok(majorList);
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/createMajor")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> createMajor(@RequestBody Major major) {
        try {
            Firestore firestore = FirestoreClient.getFirestore();

            Query query = FirestoreClient.getFirestore().collection("majors")
                    .whereEqualTo("majorName", major.getMajorName());
            QuerySnapshot querySnapshot = query.get().get();

            if (!querySnapshot.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User Already Exists"));
            }

            String id = UUID.randomUUID().toString();
            major.setId(id);

            FirebaseAuth.getInstance().createUser(new UserRecord.CreateRequest().setUid(id));

            FirestoreClient.getFirestore().collection("majors").document(id).set(major);


            return ResponseEntity.ok("Major created and added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }


    }

    @PutMapping("/updateSchedule")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> updateSchedule(@RequestBody Major major) {
        try {
            System.out.println("update here");
            Firestore firestore = FirestoreClient.getFirestore();
            CollectionReference collectionRef = firestore.collection("majors");
            Query query = collectionRef.whereEqualTo("majorName", major.getMajorName());
            ApiFuture<QuerySnapshot> future = query.get();
            QuerySnapshot querySnapshot = future.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

            if (documents.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            DocumentSnapshot document = documents.get(0);
            String documentId = document.getId();
            firestore.collection("majors").document(documentId).set(major);

            return ResponseEntity.ok("Schedule updated successfully");
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PutMapping("/updateScheduleEditStatus")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> updateScheduleEditStatus(@RequestParam String id, @RequestParam boolean status) {
        try {
            Firestore firestore = FirestoreClient.getFirestore();
            DocumentReference documentRef = firestore.collection("users").document(id);
            ApiFuture<DocumentSnapshot> future = documentRef.get();
            DocumentSnapshot document = future.get();


            documentRef.update("editingSchedule", status);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Schedule updated successfully");
            ObjectMapper mapper = new ObjectMapper();
            String jsonResponse = mapper.writeValueAsString(response);

            // Set the response content type as JSON
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            return new ResponseEntity<>(jsonResponse, headers, HttpStatus.OK);
        } catch (InterruptedException | ExecutionException | JsonProcessingException e) {
            // Log the error for debugging purposes
            e.printStackTrace();

            // Return a custom error response
            String errorMessage = "Failed to update schedule.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }


    @GetMapping("/getCourseDetails")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Course>> getCourseDetails(@RequestParam String id) {
        try {
            ApiFuture<QuerySnapshot> future = FirestoreClient.getFirestore().collection("courses").whereEqualTo("id", id).get();
            List<Course> courseList = new ArrayList<>();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                Course course = document.toObject(Course.class);
                courseList.add(course);
            }
            return ResponseEntity.ok(courseList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/getUserCourses")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<String>> getUserCourses(@RequestParam String email) {
        try {
            List<String> courseIdList = new ArrayList<>();

            // Assuming you have a Firestore instance named "firestore"
            Firestore db = FirestoreClient.getFirestore();

            // Create a query to find courses where the "studentsArray" contains the specified email
            Query query = db.collection("courses").whereArrayContains("studentsArray", email);

            // Execute the query and retrieve the documents
            ApiFuture<QuerySnapshot> querySnapshotFuture = query.get();
            QuerySnapshot querySnapshot = querySnapshotFuture.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

            // Iterate through the documents and extract the course IDs
            for (QueryDocumentSnapshot document : documents) {
                String courseId = document.getId();
                courseIdList.add(courseId);
            }

            return ResponseEntity.ok(courseIdList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/getAllCourses")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Course>> getAllCourses() {
        try {
            Firestore db = FirestoreClient.getFirestore();
            CollectionReference coursesRef = db.collection("courses");
            ApiFuture<QuerySnapshot> future = coursesRef.get();
            List<Course> courseList = new ArrayList<>();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                Course course = document.toObject(Course.class);
                courseList.add(course);
            }
            return new ResponseEntity<>(courseList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/getAllUsers")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            Firestore db = FirestoreClient.getFirestore();
            CollectionReference coursesRef = db.collection("users");
            ApiFuture<QuerySnapshot> future = coursesRef.get();
            List<User> userList = new ArrayList<>();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                User user = document.toObject(User.class);
                userList.add(user);
            }
            return new ResponseEntity<>(userList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/deleteUserAdmin")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> deleteUserAdmin(@RequestParam String email) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            CollectionReference usersRef = db.collection("users");

            Query query = usersRef.whereEqualTo("email", email);
            ApiFuture<QuerySnapshot> querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

            if (!documents.isEmpty()) {
                for (DocumentSnapshot document : documents) {
                    document.getReference().delete(); // Delete the document
                }
                return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/updateUserEmailAdmin")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> updateUserEmailAdmin(@RequestBody Map<String, String> requestBody) {
        String userId = requestBody.get("userId");
        String newEmail = requestBody.get("newEmail");

        try {
            Firestore db = FirestoreClient.getFirestore();
            DocumentReference userRef = db.collection("users").document(userId);

            ApiFuture<DocumentSnapshot> future = userRef.get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                userRef.update("email", newEmail);
                User updatedUser = document.toObject(User.class);
                updatedUser.setEmail(newEmail);
                return new ResponseEntity<>(updatedUser, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/getAllCoursesAdmin")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Course>> getAllCoursesAdmin() {
        try {
            Firestore db = FirestoreClient.getFirestore();
            CollectionReference coursesRef = db.collection("courses");
            ApiFuture<QuerySnapshot> future = coursesRef.get();
            List<Course> courseList = new ArrayList<>();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                Course course = document.toObject(Course.class);
                courseList.add(course);
            }
            return new ResponseEntity<>(courseList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/deleteCourseAdmin")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> deleteCourseAdmin(@RequestParam String courseID) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            CollectionReference courseRef = db.collection("courses");

            Query query = courseRef.whereEqualTo("id", courseID);
            ApiFuture<QuerySnapshot> querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

            if (!documents.isEmpty()) {
                for (DocumentSnapshot document : documents) {
                    document.getReference().delete(); // Delete the document
                }
                return new ResponseEntity<>("Course deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Course not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/getAllMajorsAdmin")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Major>> getAllMajorsAdmin() {
        try {
            Firestore db = FirestoreClient.getFirestore();
            CollectionReference majorsRef = db.collection("majors");
            ApiFuture<QuerySnapshot> future = majorsRef.get();
            List<Major> majorList = new ArrayList<>();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                Major major = document.toObject(Major.class);
                majorList.add(major);
            }
            return new ResponseEntity<>(majorList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/getStudentCoursesList")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Course>> getStudentCoursesList(@RequestParam String email) {
        // Query the "courses" collection for courses that contain the student's email in the "studentsArray" field
        try {
            ApiFuture<QuerySnapshot> future = FirestoreClient.getFirestore()
                    .collection("courses")
                    .whereArrayContains("studentsArray", email)
                    .get();

            List<Course> courseList = new ArrayList<>();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                Course course = document.toObject(Course.class);
                courseList.add(course);
            }
            return new ResponseEntity<>(courseList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteStudentFromCourse")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<String> deleteStudentFromCourse(@RequestParam String email, @RequestParam String courseId) {
        try {
            // Retrieve the course document from Firestore
            DocumentReference courseRef = FirestoreClient.getFirestore().collection("courses").document(courseId);
            ApiFuture<DocumentSnapshot> courseFuture = courseRef.get();
            DocumentSnapshot courseSnapshot = courseFuture.get();

            // Check if the course exists
            if (courseSnapshot.exists()) {
                // Retrieve the course object
                Course course = courseSnapshot.toObject(Course.class);

                // Remove the student's email from the studentsArray field
                List<String> studentsArray = course.getStudentsArray();
                if (studentsArray != null && studentsArray.contains(email)) {
                    studentsArray.remove(email);

                    // Update the course document in Firestore
                    ApiFuture<WriteResult> updateFuture = courseRef.set(course);
                    updateFuture.get();

                    return new ResponseEntity<>("Student removed from the course successfully.", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("The student is not enrolled in the course.", HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>("Course not found.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while deleting the student from the course.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getExpensesAndIncome")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<ExpensesAndIncome>> getExpensesAndIncome(@RequestParam String userID) {
        try {
            ApiFuture<QuerySnapshot> future = FirestoreClient.getFirestore()
                    .collection("expensesAndIncome")
                    .whereEqualTo("userId", userID)
                    .get();

            List<ExpensesAndIncome> data = new ArrayList<>();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                ExpensesAndIncome item = document.toObject(ExpensesAndIncome.class);
                data.add(item);
            }
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/createExpensesAndIncome")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<String> createExpensesAndIncome(@RequestBody ExpensesAndIncome expensesAndIncome) {
        try {
            Firestore firestore = FirestoreClient.getFirestore();
            DocumentReference docRef = firestore.collection("expensesAndIncome").document();
            expensesAndIncome.setId(docRef.getId());
            ApiFuture<WriteResult> future = docRef.set(expensesAndIncome);
            future.get();
            return new ResponseEntity<>("Document created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create document", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/deleteIncomeOrExpesnse")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> deleteIncomeOrExpesnse(@RequestParam String itemID) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            CollectionReference courseRef = db.collection("expensesAndIncome");

            Query query = courseRef.whereEqualTo("id", itemID);
            ApiFuture<QuerySnapshot> querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

            if (!documents.isEmpty()) {
                for (DocumentSnapshot document : documents) {
                    document.getReference().delete(); // Delete the document
                }
                return new ResponseEntity<>("deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/createMessage")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<String> createMessage(@RequestBody ChatMessage chat) {
        try {
            Firestore firestore = FirestoreClient.getFirestore();

            // Check if a document with the same user1ID and user2ID already exists
            CollectionReference messagesCollection = firestore.collection("messages");
            Query query = messagesCollection.whereEqualTo("user1ID", chat.getUser1ID())
                    .whereEqualTo("user2ID", chat.getUser2ID());
            QuerySnapshot querySnapshot = query.get().get();

            // Check if a document with the same user2ID and user1ID already exists
            Query query2 = messagesCollection.whereEqualTo("user2ID", chat.getUser1ID())
                    .whereEqualTo("user1ID", chat.getUser2ID());
            QuerySnapshot querySnapshot2 = query2.get().get();

            if (!querySnapshot.isEmpty() || !querySnapshot2.isEmpty()) {
                // Document with the same user1ID and user2ID already exists
                return new ResponseEntity<>("Chat already exists", HttpStatus.OK);
            }

            // Create a new chat document
            DocumentReference docRef = messagesCollection.document();
            chat.setChatID(docRef.getId());
            ApiFuture<WriteResult> future = docRef.set(chat);
            future.get();

            return new ResponseEntity<>("Document created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create document", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/getMessages")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<ChatMessage>> getMessages(@RequestParam String userID1, @RequestParam String userID2) {
        try {
            Firestore firestore = FirestoreClient.getFirestore();

            // Query Firestore to get the messages
            CollectionReference messagesCollection = firestore.collection("messages");
            Query query = messagesCollection
                    .whereIn("user1ID", Arrays.asList(userID1, userID2))
                    .whereIn("user2ID", Arrays.asList(userID1, userID2));
            ApiFuture<QuerySnapshot> querySnapshot = query.get();

            List<ChatMessage> data = new ArrayList<>();
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                ChatMessage item = document.toObject(ChatMessage.class);
                data.add(item);
            }

            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/sendMessages")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<ChatMessage>> sendMessages(@RequestParam String userID1, @RequestParam String userID2, @RequestBody ChatMessage.MessageDetails[] messageDetails) {
        try {
            Firestore firestore = FirestoreClient.getFirestore();
            // Query Firestore to get the messages
            CollectionReference messagesCollection = firestore.collection("messages");
            Query query = messagesCollection
                    .whereIn("user1ID", Arrays.asList(userID1, userID2))
                    .whereIn("user2ID", Arrays.asList(userID1, userID2));
            ApiFuture<QuerySnapshot> querySnapshot = query.get();

            List<ChatMessage> data = new ArrayList<>();
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                ChatMessage item = document.toObject(ChatMessage.class);
                data.add(item);
            }

            for (ChatMessage chat : data) {
                if (messageDetails[0].getSenderID().equals(userID1)) {
                    if (chat.getMessagesArray().getUser1IDMessages().getMessage() == null) {
                        chat.getMessagesArray().getUser1IDMessages().setMessage(new ArrayList<>());
                    }
                    chat.getMessagesArray().getUser1IDMessages().getMessage().addAll(Arrays.asList(messageDetails));
                } else {
                    if (chat.getMessagesArray().getUser2IDMessages().getMessage() == null) {
                        chat.getMessagesArray().getUser2IDMessages().setMessage(new ArrayList<>());
                    }
                    chat.getMessagesArray().getUser2IDMessages().getMessage().addAll(Arrays.asList(messageDetails));
                }
                // Update the chat in Firestore
                messagesCollection.document(chat.getChatID()).set(chat);
            }

            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PostMapping("/sendMessage")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, String> request) {
        try {
            String currentChatID = request.get("currentChatID");
            String senderID = request.get("senderID");
            String message = request.get("message");

            // Query Firestore to get the chat message with the given ID
            DocumentReference messageRef = FirestoreClient.getFirestore().collection("messages").document(currentChatID);
            ApiFuture<DocumentSnapshot> messageFuture = messageRef.get();
            DocumentSnapshot messageDoc = messageFuture.get();
            if (!messageDoc.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Message Collection not found"));
            }

            // Retrieve the chat message and its messages list
            ChatMessage chatMessage = messageDoc.toObject(ChatMessage.class);
            if(chatMessage.getUser1ID().equals(senderID)){

            List<ChatMessage.MessageDetails> messagesList = chatMessage.getMessagesArray().getUser1IDMessages().getMessage();
            // Add the new message to the messages list
            ChatMessage.MessageDetails newMessage = new ChatMessage.MessageDetails(message, new Date(), senderID);
            messagesList.add(newMessage);
            }
            else
            {
                List<ChatMessage.MessageDetails> messagesList = chatMessage.getMessagesArray().getUser2IDMessages().getMessage();
                // Add the new message to the messages list
                ChatMessage.MessageDetails newMessage = new ChatMessage.MessageDetails(message, new Date(), senderID);
                messagesList.add(newMessage);
            }
            // Update the chat message in Firestore
            messageRef.set(chatMessage).get();

            return ResponseEntity.ok().body(Map.of("message", "Message sent successfully."));
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error sending message"));
        }
    }


    @PostMapping("/addZoomLink")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> addZoomLink(@RequestBody uploadZoomTeacher zoomLink) {
        try {
            String zoomLinkCourseName = zoomLink.getCourseName();

            // Get a reference to the "zoomLinks" collection in Firestore
            Firestore firestore = FirestoreClient.getFirestore();
            CollectionReference zoomLinksCollection = firestore.collection("zoomLinks");

            // Query Firestore to check if the zoom link already exists
            Query query = zoomLinksCollection.whereEqualTo("courseName", zoomLinkCourseName);
            QuerySnapshot querySnapshot = query.get().get();

            if (querySnapshot.isEmpty()) {
                // Generate a unique ID for the zoom link
                String id = UUID.randomUUID().toString();
                zoomLink.setId(id);

                // Add the zoom link to Firestore
                zoomLinksCollection.document(id).set(zoomLink);

                return ResponseEntity.status(HttpStatus.CREATED).build();
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("The zoom link already exists."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("An error occurred during the creation of the zoom link."));
        }
    }

    @DeleteMapping("/deleteZoomLink/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> deleteZoomLink(@PathVariable String id) {
        try {
            // Get a reference to the "zoomLinks" collection in Firestore
            Firestore firestore = FirestoreClient.getFirestore();
            CollectionReference zoomLinksCollection = firestore.collection("zoomLinks");

            // Check if the Zoom link with the given ID exists
            DocumentReference zoomLinkDoc = zoomLinksCollection.document(id);
            DocumentSnapshot documentSnapshot = zoomLinkDoc.get().get();

            if (documentSnapshot.exists()) {
                // Delete the Zoom link from Firestore
                zoomLinkDoc.delete();

                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("The zoom link does not exist."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("An error occurred during the deletion of the zoom link."));
        }
    }

    @GetMapping("/getAllZoomLinks")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<uploadZoomTeacher>> getAllZoomLinks() {
        try {
            Firestore db = FirestoreClient.getFirestore();
            CollectionReference coursesRef = db.collection("zoomLinks");
            ApiFuture<QuerySnapshot> future = coursesRef.get();
            List<uploadZoomTeacher> courseList = new ArrayList<>();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                uploadZoomTeacher course = document.toObject(uploadZoomTeacher.class);
                courseList.add(course);
            }
            return new ResponseEntity<>(courseList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/addGrade")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> addGrade(@RequestBody Grade grade) {
        try {

            // Get a reference to the "zoomLinks" collection in Firestore
            Firestore firestore = FirestoreClient.getFirestore();
            CollectionReference zoomLinksCollection = firestore.collection("grades");

                // Generate a unique ID for the zoom link
                String id = UUID.randomUUID().toString();
                grade.setId(id);

                // Add the zoom link to Firestore
                zoomLinksCollection.document(id).set(grade);

                return ResponseEntity.status(HttpStatus.CREATED).build();


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("An error occurred during the creation of the zoom link."));
        }
    }

    @DeleteMapping("/deleteGrade/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> deleteGrade(@PathVariable String id) {
        try {
            // Get a reference to the "zoomLinks" collection in Firestore
            Firestore firestore = FirestoreClient.getFirestore();
            CollectionReference gradesCollection = firestore.collection("grades");

            // Check if the Zoom link with the given ID exists
            DocumentReference zoomLinkDoc = gradesCollection.document(id);
            DocumentSnapshot documentSnapshot = zoomLinkDoc.get().get();

            if (documentSnapshot.exists()) {
                // Delete the Zoom link from Firestore
                zoomLinkDoc.delete();

                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("The zoom link does not exist."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("An error occurred during the deletion of the zoom link."));
        }
    }

    @GetMapping("/getAllGrades")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Grade>> getAllGrades() {
        try {
            Firestore db = FirestoreClient.getFirestore();
            CollectionReference coursesRef = db.collection("grades");
            ApiFuture<QuerySnapshot> future = coursesRef.get();
            List<Grade> gardeList = new ArrayList<>();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                Grade courseU = document.toObject(Grade.class);
                gardeList.add(courseU);
            }
            return new ResponseEntity<>(gardeList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}