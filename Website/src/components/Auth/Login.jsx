import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../../api";
import logo from "../../assets/logo.png";

function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await api.post("/login", { email, password });
      const { token, role } = response.data;
      localStorage.setItem("token", token);
      localStorage.setItem("role", role);

      alert("Login successful!");
      
      // Redirect based on the role
      if (role === "Patient") {
        navigate("/predict");
      } else if (role === "Coass") {
        navigate("/pending-requests");
      } else {
        alert("Invalid role. Contact support.");
      }
    } catch (error) {
      alert("Login failed. Please check your credentials.");
    }
  };
  

  const togglePasswordVisibility = () => {
    setShowPassword((prev) => !prev);
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-green-50 to-green-100">
      <div className="bg-white shadow-lg rounded-lg p-8 w-full max-w-md">
        <div className="text-center mb-6">
          <img src={logo} alt="Logo" className="mx-auto w-20 h-20" />
          <h1 className="text-2xl font-semibold text-green-600 mt-4">Selamat Datang di TemuGigi!</h1>
          <p className="text-gray-500">Login untuk mengakses akun</p>
        </div>
        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label className="block text-gray-700 text-sm font-medium mb-2">Email</label>
            <input
              type="email"
              placeholder="Masukkan Email anda"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            />
          </div>
          <div className="mb-6 relative">
            <label className="block text-gray-700 text-sm font-medium mb-2">Password</label>
            <div className="relative">
              <input
                type={showPassword ? "text" : "password"}
                placeholder="Masukkan Password anda"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 pr-10"
              />
              <button
                type="button"
                onClick={togglePasswordVisibility}
                className="absolute inset-y-0 right-3 flex items-center text-gray-600 hover:text-green-600 focus:outline-none"
              >
                {showPassword ? (
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                    strokeWidth={1.5}
                    stroke="currentColor"
                    className="w-5 h-5"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      d="M3.98 8.318C5.855 5.11 9.31 3 12 3c2.689 0 6.145 2.11 8.02 5.318a10.477 10.477 0 011.206 2.774m-18.452 0a10.452 10.452 0 011.207-2.774m16.338 0a10.452 10.452 0 01-1.207 2.774C18.145 18.89 14.689 21 12 21c-2.69 0-6.145-2.11-8.02-5.318A10.453 10.453 0 013.98 8.318z"
                    />
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"
                    />
                  </svg>
                ) : (
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                    strokeWidth={1.5}
                    stroke="currentColor"
                    className="w-5 h-5"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      d="M3.98 8.318C5.855 5.11 9.31 3 12 3c2.689 0 6.145 2.11 8.02 5.318a10.477 10.477 0 011.206 2.774m-18.452 0a10.452 10.452 0 011.207-2.774m16.338 0a10.452 10.452 0 01-1.207 2.774C18.145 18.89 14.689 21 12 21c-2.69 0-6.145-2.11-8.02-5.318A10.453 10.453 0 013.98 8.318z"
                    />
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"
                    />
                  </svg>
                )}
              </button>
            </div>
          </div>
          <button
            type="submit"
            className="w-full bg-green-500 text-white py-2 rounded-md hover:bg-green-600 transition duration-200"
          >
            Login
          </button>
        </form>
        <Link
          to="/forgot-password"
          className="text-gray-500 hover:underline text-sm mt-4 inline-block"
        >
          Lupa Password?
        </Link>
        <div className="mt-6 text-center">
          <p className="text-gray-600">Tidak memiliki Akun?</p>
          <div className="mt-2 flex flex-col gap-2">
            <Link
              to="/register-patient"
              className="text-green-500 hover:underline"
            >
              Registrasi sebagai Pasien
            </Link>
            <Link
              to="/register-coass"
              className="text-green-500 hover:underline"
            >
              Registrasi sebagai CoAss
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Login;
