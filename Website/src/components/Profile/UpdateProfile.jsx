import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {jwtDecode} from "jwt-decode";
import api from "../../api";

function UpdateProfile() {
  const [formData, setFormData] = useState({});
  const navigate = useNavigate();

  useEffect(() => {
    const fetchProfile = async () => {
      const token = localStorage.getItem("token");

      // Jika tidak ada token, redirect ke home (/)
      if (!token) {
        navigate("/");
        return;
      }

      try {
        // Decode token untuk memastikan validitasnya
        jwtDecode(token);

        // Ambil data profil dari API
        const response = await api.get("/profile", {
          headers: { Authorization: `Bearer ${token}` },
        });

        setFormData(response.data);
      } catch (error) {
        console.error("Failed to fetch profile:", error.response?.data || error.message);
        navigate("/");
      }
    };

    fetchProfile();
  }, [navigate]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const token = localStorage.getItem("token");

    try {
      await api.put("/profile", formData, {
        headers: { Authorization: `Bearer ${token}` },
      });
      alert("Profile updated successfully!");
      navigate("/profile");
    } catch (error) {
      console.error("Failed to update profile:", error.response?.data || error.message);
      alert("Failed to update profile.");
    }
  };

  if (!formData) {
    return (
      <div className="min-h-screen flex justify-center items-center bg-green-50">
        <p className="text-gray-500 text-xl">Loading...</p>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-r from-green-100 via-green-50 to-green-100 flex items-center justify-center p-6">
      <form
        onSubmit={handleSubmit}
        className="bg-white rounded-lg shadow-lg p-8 w-full max-w-xl"
      >
        <h1 className="text-2xl font-bold text-green-600 text-center mb-6">Edit Profile</h1>

        {/* Name */}
        <div className="mb-4">
          <label className="block text-sm font-medium text-gray-700">Name</label>
          <input
            type="text"
            name="Name"
            value={formData.Name || ""}
            onChange={handleChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-green-500 focus:border-green-500"
            placeholder="Full Name"
            required
          />
        </div>

        {/* Email */}
        <div className="mb-4">
          <label className="block text-sm font-medium text-gray-700">Email</label>
          <input
            type="email"
            name="Email"
            value={formData.Email || ""}
            onChange={handleChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-green-500 focus:border-green-500"
            placeholder="example@mail.com"
            required
          />
        </div>

        {/* Gender */}
        <div className="mb-4">
          <label className="block text-sm font-medium text-gray-700">Gender</label>
          <div className="flex items-center gap-4 mt-2">
            <label className="flex items-center">
              <input
                type="radio"
                name="Gender"
                value="Male"
                checked={formData.Gender === "Male"}
                onChange={handleChange}
                className="text-green-500 focus:ring-green-500"
              />
              <span className="ml-2 text-gray-700">Male</span>
            </label>
            <label className="flex items-center">
              <input
                type="radio"
                name="Gender"
                value="Female"
                checked={formData.Gender === "Female"}
                onChange={handleChange}
                className="text-green-500 focus:ring-green-500"
              />
              <span className="ml-2 text-gray-700">Female</span>
            </label>
          </div>
        </div>

        {/* Birth Date */}
        <div className="mb-4">
          <label className="block text-sm font-medium text-gray-700">Date of Birth</label>
          <input
            type="date"
            name="Birth_date"
            value={formData.Birth_date?.split("T")[0] || ""}
            onChange={handleChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-green-500 focus:border-green-500"
            required
          />
        </div>

        {/* Phone */}
        <div className="mb-4">
          <label className="block text-sm font-medium text-gray-700">Phone</label>
          <input
            type="text"
            name="Phone"
            value={formData.Phone || ""}
            onChange={handleChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-green-500 focus:border-green-500"
            placeholder="Phone Number"
            required
          />
        </div>

        {/* Additional Fields for Coass */}
        {formData.Role === "Coass" && (
          <>
            <div className="mb-4">
              <label className="block text-sm font-medium text-gray-700">KTP</label>
              <input
                type="text"
                name="Ktp"
                value={formData.Ktp || ""}
                onChange={handleChange}
                className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-green-500 focus:border-green-500"
                placeholder="KTP Number"
              />
            </div>

            <div className="mb-4">
              <label className="block text-sm font-medium text-gray-700">NIM</label>
              <input
                type="text"
                name="Nim"
                value={formData.Nim || ""}
                onChange={handleChange}
                className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-green-500 focus:border-green-500"
                placeholder="NIM"
              />
            </div>

            <div className="mb-4">
              <label className="block text-sm font-medium text-gray-700">Practice Place</label>
              <input
                type="text"
                name="Appointment_Place"
                value={formData.Appointment_Place || ""}
                onChange={handleChange}
                className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-green-500 focus:border-green-500"
                placeholder="Practice Place"
              />
            </div>

            <div className="mb-4">
              <label className="block text-sm font-medium text-gray-700">University</label>
              <input
                type="text"
                name="University"
                value={formData.University || ""}
                onChange={handleChange}
                className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-green-500 focus:border-green-500"
                placeholder="University"
              />
            </div>
          </>
        )}

        {/* Submit Button */}
        <button
          type="submit"
          className="w-full bg-green-500 text-white p-2 rounded-lg shadow hover:bg-green-600 transition"
        >
          Update Profile
        </button>
      </form>
    </div>
  );
}

export default UpdateProfile;
