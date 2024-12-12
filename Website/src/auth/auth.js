import { jwtDecode } from "jwt-decode";

// Simpan token ke localStorage
export const setToken = (token) => {
  localStorage.setItem("token", token);
};

// Hapus token dari localStorage
export const removeToken = () => {
  localStorage.removeItem("token");
};

// Ambil token dari localStorage
export const getToken = () => {
  return localStorage.getItem("token");
};

// Decode token untuk mendapatkan payload (opsional)
export const decodeToken = (token) => {
  try {
    return jwtDecode(token);
  } catch (error) {
    console.error("Invalid token:", error);
    return null;
  }
};
