import React from "react";
import {
  Card,
  CardContent,
  CardMedia,
  Typography,
  Box,
} from "@mui/material";
import defaultImage from "../../../assets/images/no_image.png";
import type { ProductComponent } from "../../../types/types";

const ProductCard: React.FC<ProductComponent> = ({
  name,
  price,
  imageUrl,
  categoryName,
}) => {
  const imageSrc =
    imageUrl && imageUrl.trim() !== "" ? imageUrl : defaultImage;

  return (
    <Card
      sx={{
        width: 250,
        borderRadius: 3,
        boxShadow: 3,
        transition: "transform 0.2s, box-shadow 0.2s",
        "&:hover": {
          transform: "translateY(-5px)",
          boxShadow: 6,
        },
      }}
    >
    <CardMedia
  component="img"
  image={imageSrc}
  alt={name}
  onError={(e) => {
    e.currentTarget.onerror = null;
    e.currentTarget.src = defaultImage;
  }}
  sx={{
    height: 200,
    width: "100%",
    objectFit: "contain", 
    backgroundColor: "#f5f5f5", 
    borderBottom: "1px solid #eee",
    padding: 1,
  }}
/>

      <CardContent>
        <Typography variant="h6" fontWeight="bold" gutterBottom>
          {name}
        </Typography>

        <Typography variant="body1" color="text.secondary">
          ₹{price}
        </Typography>

        <Box mt={1}>
          <Typography variant="body2" color="text.secondary">
            Category: <strong>{categoryName}</strong>
          </Typography>
        </Box>
      </CardContent>
    </Card>
  );
};

export default ProductCard;
