
import { Box, Grid, Card, CardContent, Typography } from "@mui/material";
import InventoryIcon from "@mui/icons-material/Inventory";
import CategoryIcon from "@mui/icons-material/Category";
import { getAllCategories } from "../../services/api/CategoryApiService";
import { useEffect, useState } from "react";
import { getAllProducts } from "../../services/api/ProductApiService";

const AdminHome = () => {
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
      {/* Page Title */}
      <Typography variant="h4" fontWeight={600} gutterBottom>
        Admin Dashboard
      </Typography>
      <Typography variant="subtitle1" color="text.secondary" mb={4}>
        Welcome back, Admin 👋 — here’s a quick overview of your store.
      </Typography>

      {/* Stats Section */}
      <Grid container spacing={3}>
        {/* Products */}
        <Grid item xs={12} sm={6} md={3}>
          <Card sx={{ p: 2, borderRadius: 3, boxShadow: 3 }}>
            <CardContent>
              <Box display="flex" alignItems="center" gap={2}>
                <InventoryIcon sx={{ fontSize: 40, color: "primary.main" }} />
                <Box>
                  <Typography variant="h6" fontWeight={600}>
                    {numberOfProducts}
                  </Typography>
                  <Typography color="text.secondary">Products</Typography>
                </Box>
              </Box>
            </CardContent>
          </Card>
        </Grid>

        {/* Categories */}
        <Grid item xs={12} sm={6} md={3}>
          <Card sx={{ p: 2, borderRadius: 3, boxShadow: 3 }}>
            <CardContent>
              <Box display="flex" alignItems="center" gap={2}>
                <CategoryIcon sx={{ fontSize: 40, color: "secondary.main" }} />
                <Box>
                  <Typography variant="h6" fontWeight={600}>
                    {numberOfCategories}
                  </Typography>
                  <Typography color="text.secondary">Categories</Typography>
                </Box>
              </Box>
            </CardContent>
          </Card>
        </Grid>

       
      </Grid>



    </Box>
  );
};

export default AdminHome;
