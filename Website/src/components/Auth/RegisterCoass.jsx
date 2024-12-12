import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../api";

function RegisterCoass() {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    confirmPassword: "",
    name: "",
    gender: "Male",
    birth_date: "",
    phone: "",
    ktp: "",
    nim: "",
    appointment_place: "",
    university: "",
  });

  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (formData.password !== formData.confirmPassword) {
      alert("Passwords do not match!");
      return;
    }
    try {
      await api.post("/registercoass", formData);
      alert("CoAss registration successful!");
      navigate("/");
    } catch (error) {
      alert("CoAss registration failed!");
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-r from-green-100 via-green-50 to-green-100 flex items-center justify-center">
      <form onSubmit={handleSubmit} className="bg-white shadow-xl rounded-lg p-8 max-w-lg w-full">
        <h1 className="text-2xl font-bold text-center text-green-600 mb-6">Register CoAss</h1>

        {/* Name */}
        <div className="mb-4">
          <label className="block text-sm font-medium text-gray-700">Name</label>
          <input
            type="text"
            name="name"
            placeholder="Full Name"
            value={formData.name}
            onChange={handleChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-green-500 focus:border-green-500"
            required
          />
        </div>

        {/* Email */}
        <div className="mb-4">
          <label className="block text-sm font-medium text-gray-700">Email</label>
          <input
            type="email"
            name="email"
            placeholder="example@mail.com"
            value={formData.email}
            onChange={handleChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-green-500 focus:border-green-500"
            required
          />
        </div>

        {/* Password */}
        <div className="mb-4">
          <label className="block text-sm font-medium text-gray-700">Password</label>
          <input
            type="password"
            name="password"
            placeholder="Password"
            value={formData.password}
            onChange={handleChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-green-500 focus:border-green-500"
            required
          />
        </div>

        {/* Confirm Password */}
        <div className="mb-4">
          <label className="block text-sm font-medium text-gray-700">Confirm Password</label>
          <input
            type="password"
            name="confirmPassword"
            placeholder="Confirm Password"
            value={formData.confirmPassword}
            onChange={handleChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-green-500 focus:border-green-500"
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
                name="gender"
                value="Male"
                checked={formData.gender === "Male"}
                onChange={handleChange}
                className="text-green-500 focus:ring-green-500"
              />
              <span className="ml-2 text-gray-700">Male</span>
            </label>
            <label className="flex items-center">
              <input
                type="radio"
                name="gender"
                value="Female"
                checked={formData.gender === "Female"}
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
            name="birth_date"
            value={formData.birth_date}
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
            name="phone"
            placeholder="Phone Number"
            value={formData.phone}
            onChange={handleChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-green-500 focus:border-green-500"
          />
        </div>

        {/* KTP */}
        <div className="mb-4">
          <label className="block text-sm font-medium text-gray-700">KTP Number</label>
          <input
            type="text"
            name="ktp"
            placeholder="KTP Number"
            value={formData.ktp}
            onChange={handleChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-green-500 focus:border-green-500"
            required
          />
        </div>

        {/* NIM */}
        <div className="mb-4">
          <label className="block text-sm font-medium text-gray-700">NIM</label>
          <input
            type="text"
            name="nim"
            placeholder="NIM"
            value={formData.nim}
            onChange={handleChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-green-500 focus:border-green-500"
            required
          />
        </div>

        {/* Appointment Place */}
        <div className="mb-4">
          <label className="block text-sm font-medium text-gray-700">Practice Place</label>
          <input
            type="text"
            name="appointment_place"
            placeholder="Practice Place"
            value={formData.appointment_place}
            onChange={handleChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-green-500 focus:border-green-500"
            required
          />
        </div>

        {/* University */}
        <div className="mb-6">
          <label className="block text-sm font-medium text-gray-700">University</label>
          <input
            type="text"
            name="university"
            placeholder="University"
            value={formData.university}
            onChange={handleChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-green-500 focus:border-green-500"
            required
          />
        </div>

        {/* Register Button */}
        <button
          type="submit"
          className="w-full bg-green-500 text-white p-2 rounded-lg shadow hover:bg-green-600 transition mb-4"
        >
          Register
        </button>

        {/* Back to Login Button */}
        <button
          type="button"
          onClick={() => navigate("/")}
          className="w-full bg-gray-500 text-white p-2 rounded-lg shadow hover:bg-gray-600 transition"
        >
          Back to Login
        </button>
      </form>
    </div>
  );
}

export default RegisterCoass;
