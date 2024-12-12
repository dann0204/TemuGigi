import React, { useEffect, useState } from "react";
import api from "../../api";

function ViewMySchedule() {
  const [schedule, setSchedule] = useState(null);
  const [showDiagnosis, setShowDiagnosis] = useState(false); // State untuk toggle diagnosis

  // Fetch the first schedule from the API
  const fetchSchedule = async () => {
    try {
      const response = await api.get("/my-schedule", {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`, // Sertakan token untuk autentikasi
        },
      });
      setSchedule(response.data.length > 0 ? response.data[0] : null); // Ambil hanya data pertama
    } catch (error) {
      alert("Gagal memuat jadwal temu.");
    }
  };

  useEffect(() => {
    fetchSchedule();
  }, []);

  // Format date and time
  const formatDate = (dateString) => {
    const options = {
      year: "numeric",
      month: "long",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    };
    return new Date(dateString).toLocaleString("id-ID", options);
  };

  // Generate WhatsApp URL
  const generateWhatsAppURL = (phone, coassName) => {
    const message = `Hello ${coassName},\n\nI would like to confirm our appointment. Thank you!`;
    return `https://wa.me/${phone}?text=${encodeURIComponent(message)}`;
  };

  return (
    <div className="container mx-auto p-6">
      <h1 className="text-3xl font-bold text-green-700 mb-6 text-center">
        Jadwal Temu Saya
      </h1>
      <div className="max-w-md mx-auto">
        {schedule ? (
          <div className="bg-white shadow-lg rounded-lg overflow-hidden">
            <div className="p-6">
              {/* Bagian Tanggal */}
              <div className="text-center">
                <p className="text-xl font-semibold text-green-700">
                  {formatDate(schedule.Appointment_date)}
                </p>
              </div>
              {/* Gambar Profil */}
              <div className="flex justify-center mt-4">
                <div className="relative w-24 h-24">
                  <img
                    src={schedule.Coass_Img}
                    alt={schedule.Coass_name}
                    className="w-24 h-24 rounded-full object-cover border-4 border-green-500 shadow-lg"
                  />
                </div>
              </div>
              {/* Nama dan Universitas */}
              <div className="text-center mt-4">
                <p className="text-lg font-bold text-gray-800">{schedule.Coass_name}</p>
                <p className="text-sm text-gray-600 italic">{schedule.Coass_university}</p>
              </div>
              {/* Tempat Appointment */}
              <div className="mt-4 text-center">
                <p className="text-base text-gray-700">
                  <strong>Tempat:</strong> {schedule.Appointment_place}
                </p>
              </div>
              {/* Tombol untuk Toggle Diagnosis */}
              <button
                onClick={() => setShowDiagnosis(!showDiagnosis)}
                className="block text-center bg-blue-500 text-white font-semibold py-2 px-4 rounded-md hover:bg-blue-600 transition duration-200 mt-6"
              >
                {showDiagnosis ? "Sembunyikan Diagnosis" : "Detail Diagnosis yang diajukan"}
              </button>
              {/* Detail Diagnosis */}
              {showDiagnosis && (
                <div className="mt-6 bg-gray-50 p-4 rounded-lg shadow-md">
                  <h2 className="text-lg font-bold text-gray-800 text-center">Detail Diagnosis</h2>
                  <div className="flex justify-center mt-4">
                    <img
                      src={schedule.Img_disease}
                      alt={schedule.Disease_name}
                      className="w-40 h-40 object-cover border-4 border-gray-300 rounded-lg shadow-lg"
                    />
                  </div>
                  <p className="text-base text-center text-gray-700 mt-4">
                    <strong>{schedule.Disease_name}</strong>
                  </p>
                  <p className="text-sm text-gray-600 text-center italic">
                    {schedule.Description}
                  </p>
                </div>
              )}
              {/* Tombol WhatsApp */}
              <a
                href={generateWhatsAppURL(schedule.Coass_phone, schedule.Coass_name)}
                target="_blank"
                rel="noopener noreferrer"
                className="block text-center bg-green-500 text-white font-semibold py-2 px-4 rounded-md hover:bg-green-600 transition duration-200 mt-6"
              >
                Hubungi Co-Ass
              </a>
            </div>
          </div>
        ) : (
          <div className="bg-gray-100 shadow-md rounded-lg p-6 text-center">
            <p className="text-gray-500">Tidak ada jadwal temu.</p>
          </div>
        )}
      </div>
    </div>
  );
}

export default ViewMySchedule;
