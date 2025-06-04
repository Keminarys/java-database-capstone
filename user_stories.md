# User Stories for Smart Clinic Management System

---

## Admin User Stories

### **Title:**  
_As an admin, I want to log into the portal with my username and password, so that I can manage the platform securely._

**Acceptance Criteria:**  
1. Admin must enter valid credentials to log in.  
2. Admin is redirected to the admin dashboard upon successful login.  
3. Login failures are handled with error messages.

**Priority:** High  
**Story Points:** 2  
**Notes:**  
- Should implement JWT-based authentication.

---

### **Title:**  
_As an admin, I want to log out of the portal, so that I can protect system access._

**Acceptance Criteria:**  
1. Admin can log out from any page.  
2. Session is invalidated upon logout.  
3. Redirect to login page after logout.

**Priority:** High  
**Story Points:** 1  
**Notes:**  
- Ensure session is securely destroyed.

---

### **Title:**  
_As an admin, I want to add doctors to the portal, so that they can be scheduled for appointments._

**Acceptance Criteria:**  
1. Admin can fill a form to register a doctor.  
2. Doctor receives credentials via email.  
3. Data is saved in the MySQL database.

**Priority:** High  
**Story Points:** 3  
**Notes:**  
- Include validations for email and specialization.

---

### **Title:**  
_As an admin, I want to delete a doctor's profile, so that I can remove inactive staff from the platform._

**Acceptance Criteria:**  
1. Admin sees a list of doctors with delete buttons.  
2. Deletion is confirmed via modal.  
3. Deleted data is removed from the database.

**Priority:** Medium  
**Story Points:** 2  
**Notes:**  
- Deletion should also clean up associated appointments.

---

### **Title:**  
_As an admin, I want to run a stored procedure to get the number of appointments per month, so that I can track usage statistics._

**Acceptance Criteria:**  
1. Admin can execute stored procedures via SQL CLI or backend endpoint.  
2. The result shows appointments grouped by month.  
3. Data is exportable.

**Priority:** Medium  
**Story Points:** 2  
**Notes:**  
- Stored procedure must return accurate counts per doctor/month.

---

## Patient User Stories

### **Title:**  
_As a patient, I want to view a list of doctors without logging in, so that I can explore options before registering._

**Acceptance Criteria:**  
1. Public view shows all doctors and specializations.  
2. Patients can filter by specialty or location.  
3. Data is fetched from MySQL.

**Priority:** High  
**Story Points:** 3  
**Notes:**  
- Consider caching public doctor listings.

---

### **Title:**  
_As a patient, I want to sign up using my email and password, so that I can book appointments._

**Acceptance Criteria:**  
1. Signup form validates email and password strength.  
2. User receives verification or welcome message.  
3. Account is stored in the database with role “Patient”.

**Priority:** High  
**Story Points:** 3  
**Notes:**  
- Password should be hashed.

---

### **Title:**  
_As a patient, I want to log into the portal, so that I can manage my bookings._

**Acceptance Criteria:**  
1. Authenticated login using JWT.  
2. Redirect to patient dashboard upon success.  
3. Errors shown for wrong credentials.

**Priority:** High  
**Story Points:** 2  
**Notes:**  
- Reuse shared authentication services.

---

### **Title:**  
_As a patient, I want to log out of the portal, so that I can secure my account._

**Acceptance Criteria:**  
1. Logout button clears token or session.  
2. User is redirected to homepage or login page.  
3. Re-login is required after logout.

**Priority:** High  
**Story Points:** 1  
**Notes:**  
- Support logout on all devices.

---

### **Title:**  
_As a patient, I want to book an hour-long appointment, so that I can consult with a doctor._

**Acceptance Criteria:**  
1. Patient chooses available date and time.  
2. Conflicts are automatically handled.  
3. Confirmation is sent to email.

**Priority:** High  
**Story Points:** 4  
**Notes:**  
- Use calendar picker component.

---

### **Title:**  
_As a patient, I want to view my upcoming appointments, so that I can prepare accordingly._

**Acceptance Criteria:**  
1. Appointments shown with doctor name and date.  
2. Only future bookings are visible.  
3. Data is refreshed on login or booking.

**Priority:** Medium  
**Story Points:** 2  
**Notes:**  
- Add sorting and pagination for usability.

---

## Doctor User Stories

### **Title:**  
_As a doctor, I want to log into the portal, so that I can manage my appointments._

**Acceptance Criteria:**  
1. Secure login with role verification.  
2. Redirect to doctor dashboard.  
3. Errors for incorrect credentials.

**Priority:** High  
**Story Points:** 2  
**Notes:**  
- Shared login flow with patients and admin.

---

### **Title:**  
_As a doctor, I want to log out of the portal, so that I can protect my data._

**Acceptance Criteria:**  
1. Logout destroys session or token.  
2. Doctor cannot access protected pages after logout.  
3. Redirects to login page.

**Priority:** High  
**Story Points:** 1  
**Notes:**  
- Must invalidate JWT if in use.

---

### **Title:**  
_As a doctor, I want to view my appointment calendar, so that I can stay organized._

**Acceptance Criteria:**  
1. Calendar shows daily and weekly view.  
2. Appointments are color-coded.  
3. Clicking on an appointment shows details.

**Priority:** High  
**Story Points:** 4  
**Notes:**  
- Use JavaScript calendar library for frontend.

---

### **Title:**  
_As a doctor, I want to mark my unavailability, so that patients only see available slots._

**Acceptance Criteria:**  
1. Doctor selects date ranges or time slots as unavailable.  
2. Patients can’t book during blocked times.  
3. Unavailability is stored in the database.

**Priority:** High  
**Story Points:** 3  
**Notes:**  
- Auto-cancel conflicting appointments with warnings.

---

### **Title:**  
_As a doctor, I want to update my profile with specialization and contact info, so that patients have up-to-date information._

**Acceptance Criteria**
1. Doctor can access to their personnal info on their profile.  
2. Doctor can modify it.  
4. Info will be replaced in the database.

**Priority:** High  
**Story Points:** 2  
**Notes:**  
- Send a notification to the patient with a reservation.
