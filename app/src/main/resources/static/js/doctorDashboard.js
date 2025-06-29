import { getAllAppointments } from './appointmentService.js';
import { createPatientRow } from './uiHelpers.js';

const tableBody = document.getElementById('patientTableBody');
let selectedDate = new Date().toISOString().split('T')[0];
const token = localStorage.getItem('token');
let patientName = null;

document.addEventListener('DOMContentLoaded', () => {
  const searchInput = document.getElementById('searchPatient');
  const todayBtn = document.getElementById('todayBtn');
  const datePicker = document.getElementById('datePicker');

  if (searchInput) {
    searchInput.addEventListener('input', () => {
      const value = searchInput.value.trim();
      patientName = value !== '' ? value : 'null';
      loadAppointments();
    });
  }

  if (todayBtn) {
    todayBtn.addEventListener('click', () => {
      selectedDate = new Date().toISOString().split('T')[0];
      if (datePicker) datePicker.value = selectedDate;
      loadAppointments();
    });
  }

  if (datePicker) {
    datePicker.addEventListener('change', () => {
      selectedDate = datePicker.value;
      loadAppointments();
    });
  }

  renderContent();
  loadAppointments();
});

async function loadAppointments() {
  try {
    const appointments = await getAllAppointments(selectedDate, patientName || 'null', token);
    tableBody.innerHTML = '';

    if (!appointments || appointments.length === 0) {
      tableBody.innerHTML = `<tr><td colspan="4">No Appointments found for today.</td></tr>`;
      return;
    }

    appointments.forEach(app => {
      const patient = {
        id: app.id,
        name: app.patientName,
        phone: app.patientPhone,
        email: app.patientEmail,
      };
      const row = createPatientRow(patient, app);
      tableBody.appendChild(row);
    });
  } catch (error) {
    console.error('Error loading appointments:', error);
    tableBody.innerHTML = `<tr><td colspan="4">Error loading appointments. Try again later.</td></tr>`;
  }
}