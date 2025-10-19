import { useCallback, useEffect, useState } from "react";
import { Box, Grid, Container, Typography, Pagination } from "@mui/material";
import ProductCard from "../../components/User/ProductCatalog/ProductCard";
import { getAllProducts } from "../../services/api/ProductApiService";
import type { Product } from "../Admin/ProductManager";
import type { AxiosError } from "axios";
import { toast } from "react-toastify";

const UserProductManager = () => {
  const [page, setPage] = useState(0);
    const [products,setProducts] = useState<Product[]>([])
     const [rowCount, setRowCount] = useState(0); // current page
  const pageSize = 6;

  // call API with page number whenever page changes
  const handlePageChange = (event: React.ChangeEvent<unknown>, value: number) => {
    setPage(value);
    
  };

  const fetchProducts = useCallback(async () => {
    try {
      const res = await getAllProducts(page, pageSize);
      setProducts(res.data.content);
      setRowCount(res.data.totalPages);
    } catch (error) {
      const err = error as AxiosError<{ title: string }>;
      toast.error(err.response?.data?.title || "Failed to fetch products");
    }
  }, [page, pageSize]);

  useEffect(() => {
    fetchProducts()
  }, [fetchProducts]);

  return (
    <Container sx={{ mt: 4, mb: 6 }}>
      <Typography variant="h5" fontWeight={600} mb={3}>
        Product Catalog
      </Typography>

      <Grid container spacing={4}>
        {products.map((item,index) => (
          <Grid size={{ xs: 12, sm: 6, md: 4 }}>
            <ProductCard
              name={item.name}
              description={item.description}
              price={item.price}
              imageUrl={item.imageUrl}
              categoryName={item.categoryName ? item.categoryName : 'N/A'}
              key={index}
            />
          </Grid>
        ))}
      </Grid>

      <Box m={2} display="flex" justifyContent="center">
        <Pagination
          size="large"
          color="primary"
          count={rowCount} // total pages, can calculate from API
          page={page}
          onChange={handlePageChange}
        />
      </Box>
    </Container>
  );
};

export default UserProductManager;
