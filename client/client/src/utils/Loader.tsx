import { CircularProgress, Box } from "@mui/material";

const Loader = () => {
  return (
    <Box
      sx={{
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        height: "100vh",
        backgroundColor: "#fafafa",
      }}
    >
      <CircularProgress size={60} thickness={4.5} />
    </Box>
  );
};

export default Loader;
