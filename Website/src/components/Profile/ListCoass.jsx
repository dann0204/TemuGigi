import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../api";

function ListCoass() {
  const [coassList, setCoassList] = useState([]);
  const [filteredCoass, setFilteredCoass] = useState([]);
  const [searchQuery, setSearchQuery] = useState("");

  const navigate = useNavigate();
  useEffect(() => {
    const cekToken = async () => {
    const token = localStorage.getItem("token");
    if (!token) {
        navigate("/");
        return;
      }
    }
    cekToken();
  });
      
  useEffect(() => {
    const fetchCoass = async () => {
      try {
        const response = await api.get("/list-coass");
        setCoassList(response.data);
        setFilteredCoass(response.data); // Default to show all data
      } catch (error) {
        console.error("Failed to fetch Coass list:", error.response?.data);
      }
    };

    fetchCoass();
  }, []);

  // Filter data based on search query
  const handleSearch = (e) => {
    const query = e.target.value.toLowerCase();
    setSearchQuery(query);
    const filtered = coassList.filter(
      (coass) =>
        coass.Name.toLowerCase().includes(query) ||
        coass.University.toLowerCase().includes(query) ||
        coass.Appointment_Place.toLowerCase().includes(query)
    );
    setFilteredCoass(filtered);
  };

  // Generate WhatsApp URL
  const generateWhatsAppURL = (coass) => {
    const phone = coass.Phone;
    const message = `Hello ${coass.Name},\n\nI would like to discuss with you regarding your appointment at ${coass.Appointment_Place}. Thank you!`;
    return `https://wa.me/${phone}?text=${encodeURIComponent(message)}`;
  };

  return (
    <div className="min-h-screen bg-green-50 p-8">
      <h1 className="text-3xl font-bold text-green-700 mb-6 text-center">List of Co-Ass Students</h1>

      {/* Search bar */}
      <div className="mb-6">
        <input
          type="text"
          placeholder="Search by name, university, or place of practice..."
          value={searchQuery}
          onChange={handleSearch}
          className="w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
        />
      </div>

      {/* Cards for Co-Ass */}
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
        {filteredCoass.length > 0 ? (
          filteredCoass.map((coass) => (
            <div
              key={coass.Id}
              className="bg-white shadow-lg rounded-lg p-4 flex flex-col items-center text-center"
            >
              {/* Profile Picture */}
              <img
                src={coass.Img_profile || "https://via.placeholder.com/150"} // Default image if no profile picture
                alt={coass.Name}
                className="w-20 h-20 rounded-full object-cover mb-4"
              />

              {/* Name */}
              <h2 className="text-lg font-semibold text-gray-800">
                {coass.Name}
              </h2>

              {/* University */}
              <p className="text-sm text-gray-500">{coass.University}</p>
              {/* Gender */}
              <p className="text-sm text-gray-400">{coass.Gender}</p>

              {/* Place of Practice */}
              <p className="text-sm text-gray-700 mt-2">
                {coass.Appointment_Place.length > 20
                  ? `${coass.Appointment_Place.substring(0, 20)}...`
                  : coass.Appointment_Place}
                {coass.Appointment_Place.length > 20 && (
                  <span className="text-green-500 font-medium cursor-pointer">
                    {" "}
                    more
                  </span>
                )}
              </p>

              {/* Contact Button */}
              <a
                href={generateWhatsAppURL(coass)}
                target="_blank"
                rel="noopener noreferrer"
                className="mt-4 bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600 transition duration-200"
              >
                Contact
              </a>
            </div>
          ))
        ) : (
          <p className="text-gray-500 text-center col-span-full">
            No Co-Ass students found.
          </p>
        )}
      </div>
    </div>
  );
}

export default ListCoass;
