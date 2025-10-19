import { createTheme, type ThemeOptions } from "@mui/material";

export const theme: ThemeOptions = createTheme({
  typography: {
    subtitle1: {
      fontSize: 16,
      fontWeight: 700,
    },
    subtitle2: {
      fontSize: 14,
      fontWeight: 600,
    },
    body1: {
      fontSize: 14,
      fontWeight: 500,
    },
    body2: {
      fontSize: 14,
      fontWeight: 400,
    },
    caption: {
      fontSize: 12,
      fontWeight: 500,
    },
    button: {
      textTransform: "none",
      fontSize: 14,
      fontWeight: 600,
    },
  },
  palette: {
    primary: {
      main: "#012954",
    },
    secondary: {
      main: "#0748AE",
    },
    error: {
      main: "#A82E2E",
    },
    success: {
      main: "#47C746",
    },
    warning: {
      main: "#DB9900",
    },
    info: {
      main: "#AAAAAE",
    },
  },
});
