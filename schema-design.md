## Smart Clinic Management System Schema Design

## MySQL Database Design

### Table: patients
- id: INT, Primary Key, Auto Increment
- name: VARCHAR(255), Not Null
- date_of_birth: DATE, Not Null
- email: VARCHAR(255), Not Null, Unique
- phone: VARCHAR(20), Not Null, Unique
- address: TEXT

### Table: doctors
- id: INT, Primary Key, Auto Increment
- name: VARCHAR(255), Not Null
- specialization: VARCHAR(255), Not Null
- email: VARCHAR(255), Not Null, Unique
- phone: VARCHAR(20), Not Null, Unique
- available_hours: TEXT

### Table: appointments
- id: INT, Primary Key, Auto Increment
- doctor_id: INT, Foreign Key → doctors(id)
- patient_id: INT, Foreign Key → patients(id)
- appointment_time: DATETIME, Not Null
- status: INT (0 = Scheduled, 1 = Completed, 2 = Cancelled)

### Table: admin
- id: INT, Primary Key, Auto Increment
- username: VARCHAR(255), Not Null, Unique
- password: VARCHAR(255), Not Null

### Table: clinic_locations
- id: INT, Primary Key, Auto Increment
- name: VARCHAR(255), Not Null
- address: TEXT, Not Null

### Table: payments
- id: INT, Primary Key, Auto Increment
- patient_id: INT, Foreign Key → patients(id)
- amount: DECIMAL(10, 2), Not Null
- payment_date: DATETIME, Not Null
- payment_method: VARCHAR(50), Not Null



## MongoDB Collection Design

### Collection: prescriptions

{
 "_id": "ObjectId('64abc123456')",
 "patientName": "John Smith",
 "appointmentId": 51,
 "medication": "Paracetamol",
 "dosage": "500mg",
 "doctorNotes": "Take 1 tablet every 6 hours.",
 "refillCount": 2,
 "pharmacy": {
 "name": "Walgreens SF",
 "location": "Market Street"
 }
}

### Collection: messages
{
  "_id": "ObjectId('64def456789')",
  "senderId": "patient_102",
  "receiverId": "doctor_45",
  "timestamp": "2025-06-05T14:30:00Z",
  "message": "Hello Doctor, I have a question about my prescription.",
  "attachments": [
    {
      "fileName": "blood_test_results.pdf",
      "fileType": "application/pdf",
      "url": "https://clinic-files.s3.amazonaws.com/blood_test_results.pdf"
    }
  ],
  "read": false,
  "tags": ["prescription", "follow-up"],
  "metadata": {
    "urgent": true,
    "language": "en"
  }
}
### Collection: feedback
{
  "_id": "ObjectId('64fedc987654')",
  "patientId": "patient_102",
  "doctorId": "doctor_45",
  "appointmentId": 51,
  "rating": 4.5,
  "comments": "The doctor was very attentive and helpful.",
  "tags": ["professional", "friendly"],
  "submittedAt": "2025-06-05T16:45:00Z",
  "anonymous": false
}

### Collection: logs
{
  "_id": "ObjectId('64log123abc')",
  "userId": "admin_1",
  "action": "UPDATE_PATIENT_RECORD",
  "timestamp": "2025-06-05T10:15:00Z",
  "details": {
    "patientId": "patient_102",
    "fieldsChanged": ["email", "phone"]
  },
  "ipAddress": "xxx.xxx.x.xx",
  "device": "Chrome"
}

### Collection: filedUpload
{
  "_id": "ObjectId('64file789xyz')",
  "uploadedBy": "patient_102",
  "relatedTo": {
    "type": "appointment",
    "id": 51
  },
  "fileName": "someRANDOMNAME.png",
  "fileType": "image/png",
  "uploadDate": "2025-06-04T09:00:00Z",
  "url": "https://someS3bucket/someRANDOMNAME.png",
  "description": "Chest X-ray for follow-up consultation",
  "tags": ["xray", "diagnostic", "chest"]
}

