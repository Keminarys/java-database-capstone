import { openModal } from './modal.js';
import { getDoctors, saveDoctor, filterDoctors } from './doctorService.js';
import { createDoctorCard } from './uiHelpers.js';

document.addEventListener('DOMContentLoaded', () => {
  const addDoctorBtn = document.getElementById('addDoctorBtn');
  const searchInput = document.getElementById('searchDoctor');
  const timeFilter = document.getElementById('timeFilter');
  const specialtyFilter = document.getElementById('specialtyFilter');

  if (addDoctorBtn) {
    addDoctorBtn.addEventListener('click', () => openModal('addDoctor'));
  }

  if (searchInput) searchInput.addEventListener('input', filterDoctorsOnChange);
  if (timeFilter) timeFilter.addEventListener('change', filterDoctorsOnChange);
  if (specialtyFilter) specialtyFilter.addEventListener('change', filterDoctorsOnChange);

  loadDoctorCards();
});

async function loadDoctorCards() {
  try {
    const doctors = await getDoctors();
    renderDoctorCards(doctors);
  } catch (error) {
    console.error('Failed to load doctors:', error);
  }
}

async function filterDoctorsOnChange() {
  const name = document.getElementById('searchDoctor')?.value || null;
  const time = document.getElementById('timeFilter')?.value || null;
  const specialty = document.getElementById('specialtyFilter')?.value || null;

  try {
    const result = await filterDoctors(name || 'null', time || 'null', specialty || 'null');
    if (result.doctors && result.doctors.length > 0) {
      renderDoctorCards(result.doctors);
    } else {
      document.getElementById('content').innerHTML = '<p>No doctors found with the given filters.</p>';
    }
  } catch (error) {
    console.error('Error filtering doctors:', error);
    alert('An error occurred while filtering doctors.');
  }
}

function renderDoctorCards(doctors) {
  const contentDiv = document.getElementById('content');
  contentDiv.innerHTML = '';
  doctors.forEach(doctor => {
    const card = createDoctorCard(doctor);
    contentDiv.appendChild(card);
  });
}

window.adminAddDoctor = async () => {
  const name = document.getElementById('doctorName').value;
  const email = document.getElementById('doctorEmail').value;
  const phone = document.getElementById('doctorPhone').value;
  const password = document.getElementById('doctorPassword').value;
  const specialty = document.getElementById('doctorSpecialty').value;
  const availableTimes = document.getElementById('doctorTimes').value.split(',').map(t => t.trim());

  const token = localStorage.getItem('token');
  if (!token) {
    alert('Authentication token not found. Please log in again.');
    return;
  }

  const doctor = { name, email, phone, password, specialty, availableTimes };

  try {
    const result = await saveDoctor(doctor, token);
    if (result.success) {
      alert('Doctor added successfully!');
      document.getElementById('addDoctorModal').style.display = 'none';
      loadDoctorCards();
    } else {
      alert(`Failed to add doctor: ${result.message}`);
    }
  } catch (error) {
    console.error('Error adding doctor:', error);
    alert('An error occurred while adding the doctor.');
  }
};
