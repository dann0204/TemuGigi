import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import MiniNavbar from "./components/Profile/MiniNavbar";
import Login from "./components/Auth/Login";
import RegisterPatient from "./components/Auth/RegisterPatient";
import RegisterCoass from "./components/Auth/RegisterCoass";
import ForgotPassword from "./components/Auth/ForgotPassword";
import ResetPassword from "./components/Auth/ResetPassword";
import Predict from "./components/Diagnosis/Predict";
import ListCoass from "./components/Profile/ListCoass";
import ListPatients from "./components/Profile/ListPatients";
import Profile from "./components/Profile/Profile";
import UpdateProfile from "./components/Profile/UpdateProfile";
import RequestMeeting from "./components/Profile/RequestMeeting";
import ViewListPending from "./components/Profile/ViewListPending";
import ReviewPending from "./components/Profile/ReviewPending";
import ScheduleMeeting from "./components/Profile/ScheduleMeeting";
import ViewMySchedule from "./components/Profile/ViewMySchedule";
import ViewCoassSchedule from "./components/Profile/ViewCoassSchedule";
import Logout from "./components/Auth/Logout";


function App() {
  return (
    <Router>
      <MiniNavbar /> {/* Tambahkan Navbar */}
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/logout" element={<Logout />} />
        <Route path="/register-patient" element={<RegisterPatient />} />
        <Route path="/register-coass" element={<RegisterCoass />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/reset-password" element={<ResetPassword />} />
        <Route path="/predict" element={<Predict />} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/profile/edit" element={<UpdateProfile />} />
        <Route path="/list-patients" element={<ListPatients />} />
        <Route path="/list-coass" element={<ListCoass />} />
        <Route path="/request-meeting" element={<RequestMeeting />} />
        <Route path="/pending-requests" element={<ViewListPending />} />
        <Route path="/review-request" element={<ReviewPending />} />
        <Route path="/schedule-meeting" element={<ScheduleMeeting />} />
        <Route path="/my-schedule" element={<ViewMySchedule/>} />
        <Route path="/coass-schedule" element={<ViewCoassSchedule />} />
        <Route path="*" element={<Login />} />
      </Routes>
    </Router>
  );
}

export default App;


