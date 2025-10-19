import React, { useEffect, useState } from "react";
import { Grid, Paper, Typography, Box, Button } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { getAllCategories } from "../../services/api/CategoryApiService";
import { getAllProducts } from "../../services/api/ProductApiService";

const Home = () => {
  const navigate = useNavigate();
  const [numberOfCategories,setNumberOfCategories] = useState(0)
  const [numberOfProducts,setNumberOfProducts] = useState(0)
  const fetchCategories = async ()=>{
    const response = await getAllCategories(0,6)
   
    setNumberOfCategories(response.data.totalElements)

  }
  const fetchProducts = async()=>{
    const response = await getAllProducts(0,6)
      setNumberOfProducts(response.data.totalElements)
  }

  useEffect(()=>{
    fetchCategories()
    fetchProducts()
  },[])
  return (
    <Box sx={{ p: 4 }}>
      {/* Welcome Header */}
      <Typography variant="h4" fontWeight={600} gutterBottom>
        Welcome back, User 👋
      </Typography>
      <Typography variant="body1" color="text.secondary" mb={4}>
        Explore products, discover new categories, and manage your cart easily.
      </Typography>

      {/* Dashboard Stats */}
      <Grid container spacing={3}>
        <Grid item xs={12} sm={6} md={4}>
          <Paper
            elevation={3}
            sx={{
              p: 3,
              textAlign: "center",
              borderRadius: 3,
              transition: "0.3s",
              "&:hover": { boxShadow: 6, transform: "translateY(-3px)" },
            }}
          >
            <Typography variant="h6" fontWeight={500}>
              Total Products
            </Typography>
            <Typography variant="h4" color="primary" fontWeight={700}>
              {numberOfProducts}
            </Typography>
          </Paper>
        </Grid>

        <Grid item xs={12} sm={6} md={4}>
          <Paper
            elevation={3}
            sx={{
              p: 3,
              textAlign: "center",
              borderRadius: 3,
              transition: "0.3s",
              "&:hover": { boxShadow: 6, transform: "translateY(-3px)" },
            }}
          >
            <Typography variant="h6" fontWeight={500}>
              Categories
            </Typography>
            <Typography variant="h4" color="secondary" fontWeight={700}>
              {numberOfCategories}
            </Typography>
          </Paper>
        </Grid>

        
      </Grid>

      {/* Quick Actions */}
      <Box mt={5} textAlign="center">
        <Typography variant="h6" fontWeight={600} mb={2}>
          Quick Actions
        </Typography>
        <Button
          variant="contained"
          color="primary"
          sx={{ m: 1 }}
          onClick={() => navigate("/user/products")}
        >
          View Products
        </Button>
       
      </Box>
    </Box>
  );
};

export default Home;
