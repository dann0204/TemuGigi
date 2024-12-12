import React, { useState } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import api from "../../api";

function ResetPassword() {
  const [searchParams] = useSearchParams();
  const token = searchParams.get("token");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate(); // Untuk redirect pengguna

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (newPassword !== confirmPassword) {
      alert("Passwords do not match!");
      return;
    }

    setLoading(true);

    try {
      await api.post("/reset-password", { token, newPassword });
      // Menampilkan pop-up sukses
      alert("Password reset successfully! Redirecting to login page...");
      navigate("/login"); // Redirect ke halaman login
    } catch (error) {
      alert(
        error.response?.data?.message || "Failed to reset password. Please try again."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-green-100 to-green-300">
      <form
        onSubmit={handleSubmit}
        className="bg-white p-8 rounded-lg shadow-lg w-full max-w-md"
      >
        <h1 className="text-2xl font-bold text-center mb-6 text-green-600">
          Reset Password
        </h1>
        <div className="mb-4">
          <label className="block text-sm font-semibold mb-2 text-gray-700">
            New Password
          </label>
          <input
            type="password"
            placeholder="Enter new password"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
            className="w-full p-3 border rounded focus:ring-2 focus:ring-green-500 focus:outline-none"
            required
          />
        </div>
        <div className="mb-4">
          <label className="block text-sm font-semibold mb-2 text-gray-700">
            Confirm Password
          </label>
          <input
            type="password"
            placeholder="Confirm your password"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
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
          {loading ? "Resetting Password..." : "Reset Password"}
        </button>
      </form>
    </div>
  );
}

export default ResetPassword;
