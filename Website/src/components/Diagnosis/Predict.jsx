import React, { useEffect, useState } from "react";
import api from "../../api";
import { useNavigate } from "react-router-dom";

function Predict() {
  const [diagnosis, setDiagnosis] = useState(null);
  const [image, setImage] = useState(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState(null);
  const [showLoadingPopup, setShowLoadingPopup] = useState(false);

  const navigate = useNavigate();

  // Check token and redirect if unauthorized
  useEffect(() => {
    const checkToken = () => {
      const token = localStorage.getItem("token");
      if (!token) {
        navigate("/");
      }
    };
    checkToken();
  }, [navigate]);

  // Fetch the latest diagnosis
  const fetchDiagnosis = async () => {
    try {
      const response = await api.get("/predict");
      setDiagnosis(response.data[0]); // Set the first diagnosis (if available)
    } catch (err) {
      if (err.response?.status === 404) {
        setDiagnosis(null); // No diagnosis found
      } else {
        console.error("Failed to fetch diagnosis:", err.response?.data);
        setError("An error occurred while fetching diagnosis.");
      }
    }
  };

  // Fetch the latest diagnosis on component mount
  useEffect(() => {
    fetchDiagnosis();
  }, []);

  // Handle file upload
  const handleFileChange = (event) => {
    setImage(event.target.files[0]);
  };

  // Submit the file for analysis
  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!image) {
      setError("Please select an image.");
      return;
    }

    setIsSubmitting(true);
    setError(null);

    const formData = new FormData();
    formData.append("Img_disease", image); // Ensure key matches backend key

    setShowLoadingPopup(true); // Show loading popup
    setTimeout(async () => {
      try {
        await api.post("/predict", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        });

        // Refresh diagnosis data after successful upload
        await fetchDiagnosis(); // Fetch the latest diagnosis
        setImage(null); // Reset the image input
        alert("Berhasil Mendiagnosis!");
      } catch (err) {
        console.error("Failed to detect Image", err.response?.data);
        setError("Model Tidak dapat mendeteksi Foto, Tolong Gunakan Foto Gigi yang benar");
      } finally {
        setIsSubmitting(false);
        setShowLoadingPopup(false); // Hide loading popup
      }
    }, 1000); // Delay for 1 seconds
  };

  return (
    <div className="min-h-screen bg-green-50 p-8">
      <h1 className="text-3xl font-bold text-green-700 mb-6 text-center">Predict Dental Diseases</h1>

      {/* Card for GET /predict */}
      <div className="bg-white shadow-lg rounded-lg p-6 mb-6">
        <h2 className="text-xl font-semibold text-gray-800 mb-4">Latest Diagnosis</h2>
        {diagnosis ? (
          <div>
            <img
              src={diagnosis.Img_disease}
              alt="Diagnosis"
              className="w-full h-64 object-cover mb-4 rounded-lg"
              style={{ maxHeight: "300px", objectFit: "contain" }}
            />
            <p><strong>Disease Name:</strong> {diagnosis.Disease_name}</p>
            <p><strong>Description:</strong> {diagnosis.Description}</p>
            <p><strong>Diagnosis Date:</strong> {new Date(diagnosis.Diagnosis_date).toLocaleString()}</p>
            <p><strong>Model Version:</strong> {diagnosis.Model_version}</p>
          </div>
        ) : (
          <p className="text-gray-500">Anda tidak memiliki riwayat sama sekali</p>
        )}
      </div>

      {/* Card for POST /predict */}
      <div className="bg-white shadow-lg rounded-lg p-6">
        <h2 className="text-xl font-semibold text-gray-800 mb-4">Upload Gambar Gigi anda untuk Diagnosis</h2>
        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <input
              type="file"
              accept="image/*"
              onChange={handleFileChange}
              className="w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            />
          </div>
          {error && <p className="text-red-500 mb-4">{error}</p>}
          <button
            type="submit"
            className={`px-6 py-2 rounded-md text-white ${isSubmitting ? "bg-gray-400" : "bg-green-500 hover:bg-green-600"} transition duration-200`}
            disabled={isSubmitting}
          >
            {image ? (isSubmitting ? "Model Sedang Mendeteksi..." : "Cek Kondisi Gigi") : "Upload Gambar terlebih dahulu"}
          </button>
        </form>
      </div>

      {/* Loading Popup */}
      {showLoadingPopup && (
        <div className="fixed inset-0 bg-gray-800 bg-opacity-50 flex justify-center items-center">
          <div className="bg-white rounded-lg p-6 text-center shadow-lg">
            <p className="text-lg font-semibold text-gray-800">Proses Diagnosis...</p>
          </div>
        </div>
      )}
    </div>
  );
}

export default Predict;
