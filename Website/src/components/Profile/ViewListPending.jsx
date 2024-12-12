import React, { useEffect, useState } from "react";
import api from "../../api";

function PendingRequests() {
  const [pendingRequests, setPendingRequests] = useState([]);
  const [selectedRequest, setSelectedRequest] = useState(null);
  const [loading, setLoading] = useState(false);

  // Fetch pending requests from API
  const fetchPendingRequests = async () => {
    try {
      const response = await api.get("/pending-requests");
      setPendingRequests(response.data);
    } catch (error) {
      alert("Gagal memuat permintaan pending.");
    }
  };

  // Handle accept/reject action
  const handleAction = async (status) => {
    if (!selectedRequest) return;
    setLoading(true);
    try {
      await api.put("/review-request", {
        request_id: selectedRequest.Request_id,
        status,
      });
      alert(`Permintaan telah ${status === "Accepted" ? "diterima" : "ditolak"}.`);
      setSelectedRequest(null);
      fetchPendingRequests(); // Refresh list
    } catch (error) {
      alert("Gagal memperbarui status permintaan.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchPendingRequests();
  }, []);

  return (
    <div className="container mx-auto p-6">
      <h1 className="text-2xl font-semibold mb-4">Permintaan Pending</h1>
      {pendingRequests.length > 0 ? (
        <table className="w-full table-auto border-collapse border border-gray-200">
          <thead>
            <tr>
              <th className="border border-gray-200 px-4 py-2">No</th>
              <th className="border border-gray-200 px-4 py-2">Name</th>
              <th className="border border-gray-200 px-4 py-2">Disease Name</th>
              <th className="border border-gray-200 px-4 py-2">Action</th>
            </tr>
          </thead>
          <tbody>
            {pendingRequests.map((request, index) => (
              <tr key={request.Request_id}>
                <td className="border border-gray-200 px-4 py-2 text-center">{index + 1}</td>
                <td className="border border-gray-200 px-4 py-2">{request.Name}</td>
                <td className="border border-gray-200 px-4 py-2">{request.Disease_name}</td>
                <td className="border border-gray-200 px-4 py-2 text-center">
                  <button
                    onClick={() => setSelectedRequest(request)}
                    className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                  >
                    Details
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p>Tidak ada permintaan dari pasien saat ini.</p>
      )}

      {/* Modal for Request Details */}
      {selectedRequest && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
          <div className="bg-white p-6 rounded shadow-lg w-full max-w-lg relative">
            <h2 className="text-xl font-semibold mb-4">Detail Permintaan</h2>
            <div className="mb-4">
              <p className="font-medium">Name         : {selectedRequest.Name}</p>
              <p className="font-medium">Disease Name : {selectedRequest.Disease_name}</p>
              <p className="text-gray-600">Description  : {selectedRequest.Description}</p>
              <p className="text-gray-600">Diajukan pada: {new Date(selectedRequest.Requested_at).toLocaleString()}</p>
            </div>
            <div className="mb-4">
              <img
                src={selectedRequest.Img_disease}
                alt="Disease"
                className="w-full h-auto rounded border"
              />
            </div>
            <div className="flex justify-end space-x-4">
              <button
                onClick={() => handleAction("Accepted")}
                className={`bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 ${
                  loading && "opacity-50"
                }`}
                disabled={loading}
              >
                {loading ? "Processing..." : "Accept"}
              </button>
              <button
                onClick={() => handleAction("Rejected")}
                className={`bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 ${
                  loading && "opacity-50"
                }`}
                disabled={loading}
              >
                {loading ? "Processing..." : "Reject"}
              </button>
              <button
                onClick={() => setSelectedRequest(null)}
                className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600"
              >
                Back
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default PendingRequests;
