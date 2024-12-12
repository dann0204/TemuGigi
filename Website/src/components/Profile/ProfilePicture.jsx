import React, { useState } from "react";
import api from "../../api";

const ProfilePicture = ({ onClose, onSuccess }) => {
  const [image, setImage] = useState(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState(null);

  // Handle file selection
  const handleFileChange = (event) => {
    setImage(event.target.files[0]);
  };

  // Submit the image
  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!image) {
      setError("Please select an image.");
      return;
    }

    setIsSubmitting(true);
    setError(null);

    const formData = new FormData();
    formData.append("Img_profile", image);

    try {
      const response = await api.post("/profile-picture", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
      alert("Profile picture updated successfully!");
      onSuccess(response.data); // Notify parent about success
      onClose(); // Close modal
    } catch (err) {
      console.error("Failed to upload profile picture:", err.response?.data);
      setError("An error occurred while uploading the image.");
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white p-6 rounded-lg shadow-lg w-96">
        <h2 className="text-xl font-semibold text-gray-800 mb-4">Update Profile Picture</h2>
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
          <div className="flex justify-end space-x-4">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 rounded-md bg-gray-300 hover:bg-gray-400 transition duration-200"
            >
              Cancel
            </button>
            <button
              type="submit"
              className={`px-6 py-2 rounded-md text-white ${isSubmitting ? "bg-gray-400" : "bg-green-500 hover:bg-green-600"} transition duration-200`}
              disabled={isSubmitting}
            >
              {isSubmitting ? "Submitting..." : "Submit"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ProfilePicture;
