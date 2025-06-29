import { BASE_API_URL } from './config.js';

const DOCTOR_API = `${BASE_API_URL}/doctor`;

// Fetch all doctors
export async function getDoctors() {
  try {
    const response = await fetch(DOCTOR_API);
    const data = await response.json();
    return data.doctors || [];
  } catch (error) {
    console.error('Error fetching doctors:', error);
    return [];
  }
}

// Delete a doctor by ID with token
export async function deleteDoctor(doctorId, token) {
  try {
    const response = await fetch(`${DOCTOR_API}/${doctorId}/${token}`, {
      method: 'DELETE',
    });
    const data = await response.json();
    return {
      success: response.ok,
      message: data.message || 'Unknown response from server',
    };
  } catch (error) {
    console.error('Error deleting doctor:', error);
    return {
      success: false,
      message: 'Failed to delete doctor due to a network or server error.',
    };
  }
}

// Save a new doctor with token
export async function saveDoctor(doctor, token) {
  try {
    const response = await fetch(`${DOCTOR_API}/${token}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(doctor),
    });
    const data = await response.json();
    return {
      success: response.ok,
      message: data.message || 'Doctor saved successfully.',
    };
  } catch (error) {
    console.error('Error saving doctor:', error);
    return {
      success: false,
      message: 'Failed to save doctor due to a network or server error.',
    };
  }
}

// Filter doctors by name, time, and specialty
export async function filterDoctors(name, time, specialty) {
  try {
    const response = await fetch(`${DOCTOR_API}/filter/${name}/${time}/${specialty}`);
    if (response.ok) {
      const data = await response.json();
      return data;
    } else {
      console.error('Failed to filter doctors:', response.statusText);
      return { doctors: [] };
    }
  } catch (error) {
    console.error('Error filtering doctors:', error);
    alert('An error occurred while filtering doctors. Please try again later.');
    return { doctors: [] };
  }
}