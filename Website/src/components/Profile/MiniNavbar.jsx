import React, { useState } from "react";
import { Link, useLocation } from "react-router-dom";
import logo from "../../assets/logo.png";

function MiniNavbar() {
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
  const location = useLocation();

  // Ambil role dari localStorage
  const role = localStorage.getItem("role");

  // Halaman tanpa Navbar
  const noNavbarRoutes = ["/", "/login", "/register-coass", "/register-patient", "/reset-password", "/forgot-password"];

  // Jangan tampilkan navbar jika di halaman tertentu
  if (noNavbarRoutes.includes(location.pathname)) {
    return null;
  }

  return (
    <>
      {/* Navbar */}
      <nav className="bg-gradient-to-r from-green-400 to-green-600 p-4 shadow-md fixed w-full z-50">
        <div className="container mx-auto flex justify-between items-center">
          {/* Logo */}
          <div className="flex items-center">
            <img src={logo} alt="Logo" className="w-10 h-10 mr-3" />
            <h1 className="text-xl font-bold text-white tracking-wide">
              Temu<span className="text-yellow-300">Gigi</span>
            </h1>
          </div>

          {/* Hamburger Menu */}
          <button
            className="block md:hidden text-white focus:outline-none"
            onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
          >
            <svg
              className="w-6 h-6"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth="2"
                d={isMobileMenuOpen ? "M6 18L18 6M6 6l12 12" : "M4 6h16M4 12h16m-7 6h7"}
              ></path>
            </svg>
          </button>

          {/* Links */}
          <ul
            className={`${
              isMobileMenuOpen ? "block" : "hidden"
            } md:flex space-y-4 md:space-y-0 md:space-x-6 text-white absolute md:static top-full left-0 w-full md:w-auto bg-green-600 md:bg-transparent md:shadow-none shadow-md p-4 md:p-0 transition-all`}
          >
            {role === "Patient" && (
              <>
                <li>
                  <Link to="/predict" className="hover:text-yellow-300 transition">
                    Diagnosis
                  </Link>
                </li>
                <li>
                  <Link to="/request-meeting" className="hover:text-yellow-300 transition">
                    Request Meeting
                  </Link>
                </li>
                <li>
                  <Link to="/my-schedule" className="hover:text-yellow-300 transition">
                    My Schedule
                  </Link>
                </li>
              </>
            )}

            {role === "Coass" && (
              <>
                <li>
                  <Link to="/pending-requests" className="hover:text-yellow-300 transition">
                    Pending Requests
                  </Link>
                </li>
                <li>
                  <Link to="/schedule-meeting" className="hover:text-yellow-300 transition">
                    Schedule Meeting
                  </Link>
                </li>
                <li>
                  <Link to="/coass-schedule" className="hover:text-yellow-300 transition">
                    Coass Schedule
                  </Link>
                </li>
              </>
            )}

            <li>
              <Link to="/profile" className="hover:text-yellow-300 transition">
                Profile
              </Link>
            </li>
            <li>
              <Link
                to="/logout"
                className="hover:text-red-400 transition focus:outline-none"
              >
                Logout
              </Link>
            </li>
          </ul>
        </div>
      </nav>
    </>
  );
}

export default MiniNavbar;
