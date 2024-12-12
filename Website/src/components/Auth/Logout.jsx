import React from "react";
import { useNavigate } from "react-router-dom";

function Logout() {
  const navigate = useNavigate();

  const handleConfirmLogout = () => {
    localStorage.removeItem("token"); // Hapus token dari localStorage
    localStorage.removeItem("role"); // Hapus role dari localStorage
    navigate("/login"); // Redirect ke halaman login
  };

  const handleCancelLogout = () => {
    navigate(-1); // Kembali ke halaman sebelumnya
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-lg p-6 w-11/12 max-w-md">
        <h2 className="text-lg font-bold text-gray-800">Konfirmasi Logout</h2>
        <p className="text-gray-600 mt-2">Apakah Anda yakin ingin keluar?</p>
        <div className="flex justify-end mt-4 space-x-4">
          <button
            className="px-4 py-2 bg-gray-200 text-gray-800 rounded hover:bg-gray-300 transition"
            onClick={handleCancelLogout}
          >
            Batal
          </button>
          <button
            className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600 transition"
            onClick={handleConfirmLogout}
          >
            Keluar
          </button>
        </div>
      </div>
    </div>
  );
}

export default Logout;
