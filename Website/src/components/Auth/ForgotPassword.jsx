import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../api";

function ForgotPassword() {
  const [email, setEmail] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate(); // Untuk redirect pengguna

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true); // Mengunci tombol submit

    try {
      await api.post("/forgot-password", { email });
      alert("Password reset link telah dikirim ke email Anda. Silakan cek email Anda!");
      navigate("/login"); // Redirect ke halaman login setelah sukses
    } catch (error) {
      alert("Gagal mengirimkan reset link. Silakan coba lagi.");
    } finally {
      setLoading(false); // Membuka tombol submit setelah selesai
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-green-100 to-green-300">
      <form
        onSubmit={handleSubmit}
        className="bg-white p-6 rounded shadow-lg w-full max-w-md"
      >
        <h1 className="text-2xl font-bold text-center mb-6 text-green-600">
          Forgot Password
        </h1>
        <div className="mb-4">
          <label className="block text-sm font-semibold mb-2 text-gray-700">
            Email Address
          </label>
          <input
            type="email"
            placeholder="Enter your email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="w-full p-3 border rounded focus:ring-2 focus:ring-green-500 focus:outline-none"
            required
          />
        </div>
        <button
          type="submit"
          className={`w-full p-3 text-white rounded bg-green-500 ${
            loading ? "opacity-50 cursor-not-allowed" : "hover:bg-green-600"
          }`}
          disabled={loading}
        >
          {loading ? "Sending Reset Link..." : "Submit"}
        </button>
        <button
          type="button"
          onClick={() => navigate("/login")}
          className="w-full mt-4 p-3 text-green-500 border border-green-500 rounded bg-white hover:bg-green-100"
        >
          Back to Login
        </button>
      </form>
    </div>
  );
}

export default ForgotPassword;
