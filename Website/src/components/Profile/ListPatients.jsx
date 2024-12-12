import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {jwtDecode} from "jwt-decode";
import api from "../../api";

function ListPatients() {
  const [patientsList, setPatientsList] = useState([]);
  const [filteredPatients, setFilteredPatients] = useState([]);
  const [searchTerm, setSearchTerm] = useState({
    name: "",
    disease: "",
    city: "",
    gender: "",
  });
  const [selectedPatient, setSelectedPatient] = useState(null);
  const [showPopup, setShowPopup] = useState(false);
  const [accessDenied, setAccessDenied] = useState(false);

  const navigate = useNavigate();

  useEffect(() => {
    // Validate JWT and Role
    const validateAccess = () => {
      const token = localStorage.getItem("token");
      if (!token) {
        navigate("/");
        return;
      }

      try {
        const decodedToken = jwtDecode(token);
        if (decodedToken.role !== "Coass") {
          setAccessDenied(true);
          return;
        }
      } catch (error) {
        console.error("Invalid JWT:", error);
        setAccessDenied(true);
      }
    };

    validateAccess();
  }, );

  useEffect(() => {
    if (!accessDenied) {
      const fetchPatients = async () => {
        try {
          const response = await api.get("/list-patients");
          setPatientsList(response.data);
          setFilteredPatients(response.data);
        } catch (error) {
          console.error("Failed to fetch Patients list:", error.response.data);
        }
      };

      fetchPatients();
    }
  }, [accessDenied]);

  const handleSearch = () => {
    const filtered = patientsList.filter((patient) => {
      const nameMatch = patient.Name.toLowerCase().includes(
        searchTerm.name.toLowerCase()
      );
      const diseaseMatch = patient.Disease_name.toLowerCase().includes(
        searchTerm.disease.toLowerCase()
      );
      const cityMatch = patient.City.toLowerCase().includes(
        searchTerm.city.toLowerCase()
      );
      const genderMatch = searchTerm.gender
        ? patient.Gender.toLowerCase() === searchTerm.gender.toLowerCase()
        : true;

      return nameMatch && diseaseMatch && cityMatch && genderMatch;
    });

    setFilteredPatients(filtered);
  };

  const openPopup = (patient) => {
    setSelectedPatient(patient);
    setShowPopup(true);
  };

  const closePopup = () => {
    setShowPopup(false);
    setSelectedPatient(null);
  };

  const handleCloseAccessDeniedPopup = () => {
    setAccessDenied(false);
    navigate("/"); // Redirect to login page or home
  };

  return (
    <div className="min-h-screen bg-green-50 p-4">
      {accessDenied ? (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">
          <div className="bg-white p-6 rounded shadow w-4/5 md:w-1/3">
            <h2 className="text-xl font-bold text-red-500 mb-4">Dilarang Masuk</h2>
            <p className="mb-4">
              Anda tidak memiliki akses untuk melihat halaman ini. Silakan login
              dengan akun yang memiliki izin.
            </p>
            <button
              onClick={handleCloseAccessDeniedPopup}
              className="bg-green-500 text-white px-4 py-2 rounded"
            >
              Kembali ke Halaman Utama
            </button>
          </div>
        </div>
      ) : (
        <>
          <h1 className="text-2xl font-bold text-green-700 mb-4">List of Patients</h1>

          {/* Search Filters */}
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
            <input
              type="text"
              placeholder="Search by Name"
              value={searchTerm.name}
              onChange={(e) =>
                setSearchTerm({ ...searchTerm, name: e.target.value })
              }
              className="p-2 border rounded"
            />
            <input
              type="text"
              placeholder="Search by Disease"
              value={searchTerm.disease}
              onChange={(e) =>
                setSearchTerm({ ...searchTerm, disease: e.target.value })
              }
              className="p-2 border rounded"
            />
            <input
              type="text"
              placeholder="Search by City"
              value={searchTerm.city}
              onChange={(e) =>
                setSearchTerm({ ...searchTerm, city: e.target.value })
              }
              className="p-2 border rounded"
            />
            <select
              value={searchTerm.gender}
              onChange={(e) =>
                setSearchTerm({ ...searchTerm, gender: e.target.value })
              }
              className="p-2 border rounded"
            >
              <option value="">All Genders</option>
              <option value="Male">Male</option>
              <option value="Female">Female</option>
            </select>
            <button
              onClick={handleSearch}
              className="col-span-2 md:col-span-1 bg-green-500 text-white p-2 rounded"
            >
              Search
            </button>
          </div>

          {/* Patients Table */}
          <table className="w-full bg-white rounded shadow">
            <thead>
              <tr>
                <th className="p-2 border">Name</th>
                <th className="p-2 border">City</th>
                <th className="p-2 border">Gender</th>
                <th className="p-2 border">Phone</th>
                <th className="p-2 border">Disease</th>
                <th className="p-2 border">Actions</th>
              </tr>
            </thead>
            <tbody>
              {filteredPatients.map((patient) => (
                <tr key={patient.Id}>
                  <td className="p-2 border">{patient.Name}</td>
                  <td className="p-2 border">{patient.City}</td>
                  <td className="p-2 border">{patient.Gender}</td>
                  <td className="p-2 border">{patient.Phone}</td>
                  <td className="p-2 border">{patient.Disease_name}</td>
                  <td className="p-2 border">
                    <button
                      onClick={() => openPopup(patient)}
                      className="bg-green-500 text-white px-2 py-1 rounded"
                    >
                      Details
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          {/* Patient Details Popup */}
          {showPopup && selectedPatient && (
            <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">
              <div className="bg-white p-6 rounded shadow w-4/5 md:w-1/3">
                <h2 className="text-xl font-bold text-green-700 mb-4">
                  {selectedPatient.Name}'s Details
                </h2>
                <p>
                  <strong>Gender:</strong> {selectedPatient.Gender}
                </p>
                <p>
                  <strong>City:</strong> {selectedPatient.City}
                </p>
                <p>
                  <strong>Phone:</strong> {selectedPatient.Phone}
                </p>
                <p>
                  <strong>Disease:</strong> {selectedPatient.Disease_name}
                </p>
                <img
                  src={selectedPatient.Img_disease}
                  alt="Disease"
                  className="w-full h-40 object-cover mt-2 mb-4"
                />
                <p>
                  <strong>Description:</strong> {selectedPatient.Description}
                </p>
                <p>
                  <strong>Diagnosis Date:</strong>{" "}
                  {new Date(
                    selectedPatient.Diagnosis_date
                  ).toLocaleString()}
                </p>
                <p>
                  <strong>Model Version:</strong> {selectedPatient.Model_version}
                </p>
                <button
                  onClick={closePopup}
                  className="mt-4 bg-red-500 text-white px-4 py-2 rounded"
                >
                  Close
                </button>
              </div>
            </div>
          )}
        </>
      )}
    </div>
  );
}

export default ListPatients;
