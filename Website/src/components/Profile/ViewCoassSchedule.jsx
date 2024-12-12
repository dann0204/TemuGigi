import React, { useEffect, useState } from "react";
import api from "../../api";

function CoassSchedule() {
  const [schedules, setSchedules] = useState([]);
  const [expandedRow, setExpandedRow] = useState(null); // Untuk melacak baris yang diperluas

  // Fetch schedules from API
  const fetchSchedules = async () => {
    try {
      const response = await api.get("/coass-schedule", {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`, // Sertakan token untuk autentikasi
        },
      });
      setSchedules(response.data);
    } catch (error) {
      alert("Gagal memuat jadwal temu.");
    }
  };

  useEffect(() => {
    fetchSchedules();
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

  // Handle row expansion toggle
  const toggleDetails = (id) => {
    setExpandedRow((prev) => (prev === id ? null : id));
  };

  return (
    <div className="container mx-auto p-6">
      <h1 className="text-3xl font-bold text-green-700 mb-6 text-center">
        Jadwal Temu Saya
      </h1>
      {schedules.length > 0 ? (
        <div className="overflow-x-auto">
          <table className="table-auto w-full bg-white shadow-md rounded-lg">
            <thead className="bg-green-500 text-white">
              <tr>
                <th className="px-4 py-2">No</th>
                <th className="px-4 py-2">Appointment Date</th>
                <th className="px-4 py-2">Place</th>
                <th className="px-4 py-2">Patient Name</th>
                <th className="px-4 py-2">Disease Name</th>
                <th className="px-4 py-2">Details</th>
              </tr>
            </thead>
            <tbody>
              {schedules.map((schedule, index) => (
                <React.Fragment key={schedule.Schedule_id}>
                  <tr className="text-center border-t hover:bg-gray-100">
                    <td className="px-4 py-2 font-semibold">{index + 1}</td>
                    <td className="px-4 py-2 text-green-700 font-bold">
                      {formatDate(schedule.Appointment_date)}
                    </td>
                    <td className="px-4 py-2 text-green-700 font-bold">
                      {schedule.Appointment_place}
                    </td>
                    <td className="px-4 py-2 text-green-700 font-bold">
                      {schedule.Patient_name}
                    </td>
                    <td className="px-4 py-2 text-green-700 font-bold">
                      {schedule.Disease_name}
                    </td>
                    <td className="px-4 py-2">
                      <button
                        onClick={() => toggleDetails(schedule.Schedule_id)}
                        className="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 transition duration-200"
                      >
                        {expandedRow === schedule.Schedule_id
                          ? "Hide Details"
                          : "Show Details"}
                      </button>
                    </td>
                  </tr>
                  {expandedRow === schedule.Schedule_id && (
                    <tr>
                      <td colSpan={6} className="px-4 py-4 bg-gray-50 text-left">
                        <div className="p-4 rounded-md shadow-md">
                          <h2 className="text-lg font-bold text-gray-800 mb-2">
                            Detail Informasi
                          </h2>
                          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            {/* Image */}
                            <div className="flex justify-center">
                              <img
                                src={schedule.Img_disease}
                                alt={schedule.Disease_name}
                                className="w-40 h-40 object-cover border-2 border-gray-300 rounded-md shadow-lg"
                              />
                            </div>
                            {/* Text Details */}
                            <div>
                              <p className="text-sm text-gray-700">
                                <strong>Gender:</strong> {schedule.Patient_gender}
                              </p>
                              <p className="text-sm text-gray-700">
                                <strong>Description:</strong> {schedule.Description}
                              </p>
                              <a
                                href={`https://wa.me/${schedule.Patient_phone}?text=${encodeURIComponent(
                                  `Hello ${schedule.Patient_name},\n\nI would like to confirm our appointment. Thank you!`
                                )}`}
                                target="_blank"
                                rel="noopener noreferrer"
                                className="block mt-4 bg-green-500 text-white text-center py-2 px-4 rounded-md hover:bg-green-600 transition duration-200"
                              >
                                Contact via WhatsApp
                              </a>
                            </div>
                          </div>
                        </div>
                      </td>
                    </tr>
                  )}
                </React.Fragment>
              ))}
            </tbody>
          </table>
        </div>
      ) : (
        <p className="text-center text-gray-500">Tidak ada jadwal temu.</p>
      )}
    </div>
  );
}

export default CoassSchedule;
