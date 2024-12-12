import React, { useEffect, useState } from "react";
import api from "../../api";

function ReviewRequest() {
  const [pendingRequests, setPendingRequests] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetchRequests = async () => {
    try {
      const response = await api.get("/pending-requests");
      setPendingRequests(response.data);
    } catch (error) {
      alert("Gagal memuat permintaan.");
    }
  };

  const handleResponse = async (requestId, status) => {
    setLoading(true);
    try {
      await api.put("/review-request", { requestId, status });
      alert(`Permintaan ${status === "Accepted" ? "diterima" : "ditolak"}.`);
      fetchRequests();
    } catch (error) {
      alert("Gagal memperbarui status permintaan.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchRequests();
  }, []);

  return (
    <div className="container mx-auto p-6">
      <h1 className="text-2xl font-semibold mb-4">Review Permintaan</h1>
      {pendingRequests.length > 0 ? (
        <ul className="space-y-4">
          {pendingRequests.map((request) => (
            <li
              key={request.request_id}
              className="p-4 border rounded shadow flex justify-between items-center"
            >
              <div>
                <h2 className="font-semibold">{request.patient_name}</h2>
                <p className="text-gray-600">Diajukan: {request.requested_at}</p>
              </div>
              <div className="flex space-x-2">
                <button
                  onClick={() => handleResponse(request.request_id, "Accepted")}
                  className={`bg-green-500 text-white px-4 py-2 rounded ${
                    loading && "opacity-50"
                  }`}
                  disabled={loading}
                >
                  Terima
                </button>
                <button
                  onClick={() => handleResponse(request.request_id, "Rejected")}
                  className={`bg-red-500 text-white px-4 py-2 rounded ${
                    loading && "opacity-50"
                  }`}
                  disabled={loading}
                >
                  Tolak
                </button>
              </div>
            </li>
          ))}
        </ul>
      ) : (
        <p>Tidak ada permintaan untuk ditinjau.</p>
      )}
    </div>
  );
}

export default ReviewRequest;
