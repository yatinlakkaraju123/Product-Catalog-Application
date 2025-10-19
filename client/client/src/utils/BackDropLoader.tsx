import { Backdrop } from "@mui/material";
import  { useState } from "react";
import Loader from "./Loader";

const BackDropLoader = () => {
  const [open, setOpen] = useState<boolean>(true);

  setTimeout(() => {
    setOpen(false);
  }, 1000);

  return (
    <Backdrop
      sx={{ color: "#fff", zIndex: (theme) => theme.zIndex.drawer + 1 }}
      open={open}
    >
      <Loader/>
    </Backdrop>
  );
};

export default BackDropLoader;
