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
      