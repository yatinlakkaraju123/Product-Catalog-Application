import React, { useState, useEffect, useCallback, useRef } from "react";
import {
  Box,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  IconButton,
  Tooltip,
  Autocomplete,
  Typography,
  CardMedia,
} from "@mui/material";
import defaultImage from "../../assets/images/no_image.png";

import { DataGrid, type GridColDef, type GridRenderCellParams } from "@mui/x-data-grid";
import { toast } from "react-toastify";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import { AxiosError } from "axios";
import { getAllCategories } from "../../services/api/CategoryApiService";
import { addProduct, deleteProduct, getAllProducts, getProductById, updateProduct } from "../../services/api/ProductApiService";
import CloudUploadIcon from '@mui/icons-material/CloudUpload';
import VisibilityOutlinedIcon from '@mui/icons-material/VisibilityOutlined';
interface Category {
  id: number;
  name: string;
}
export interface Product {
  id:number;
  name:string;
  description:string;
  price:number;
  categoryId?: number;
  categoryName?: string;
  imageUrl?:string
}
export default function ProductManager() {
  const [categories, setCategories] = useState<Category[]>([]);
  const [formData, setFormData] = useState<Product>({ id:-1,name:"",description:"",price:0 });
  const [products,setProducts] = useState<Product[]>([])
  const [page, setPage] = useState(0);
  const [pageSize, setPageSize] = useState(5);
  const [rowCount, setRowCount] = useState(0);
 const [selectedCategoryId, setSelectedCategoryId] = useState<number | null>(null);
  const [modalOpen, setModalOpen] = useState(false);
  const [viewModalOpen,setViewModalOpen] = useState(false)
  const [editingProduct, setEditingProduct] = useState<Product | null>(null);
  const [categoryName, setCategoryName] = useState("");
  const fileInputRef = useRef(null);
  const [selectedFile,setSelectedFile] = useState<File|null>(null)
  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const selectedFile = event.target.files[0] ;
    if (selectedFile) {
      setSelectedFile(selectedFile)
     // console.log('Selected file:', selectedFile.name);
      // Add logic to upload the file
    }
  };

  const handleButtonClick = () => {
    fileInputRef.current.click();
  };
  // Fetch categories from API
  const fetchCategories = async () => {
    try {
      console.log("current time in fetch categories:",new Date())
      const res = await getAllCategories(0, 1000);
      setCategories(res.data.content);
      setRowCount(res.data.totalElements);
    } catch (error) {
      const err = error as AxiosError<{ title: string }>;
      toast.error(err.response?.data?.title || "Failed to fetch categories");
    }
  };
  const fetchProducts = useCallback(async () => {
    try {
      const res = await getAllProducts(page, pageSize);
      setProducts(res.data.content);
      setRowCount(res.data.totalElements);
    } catch (error) {
      const err = error as AxiosError<{ title: string }>;
      toast.error(err.response?.data?.title || "Failed to fetch products");
      console.log("error in fetching products:",err)
    }
  }, [page, pageSize]);

  useEffect(() => {
    fetchProducts()
  }, [fetchProducts]);
  useEffect(()=>{
    fetchCategories()
  },[])
  // Open modal for add/edit
  const handleOpenModal = (product?: Product) => {
    if (product) {
      setEditingProduct(product);
      console.log("image url:",product.imageUrl)
       setFormData({
      id: product.id,
      name: product.name,
      description: product.description,
      price: product.price,
      imageUrl:product.imageUrl
    });
    setSelectedCategoryId(product.categoryId ?? null); // assuming Product has categoryId
    setSelectedFile(null); 
    } else {
      setEditingProduct(null);
      setFormData({
      id: 0,
      name: "",
      description: "",
      price: 0,
    });
    }
    setModalOpen(true);
  };

  const handleCloseModal = () => setModalOpen(false);
  const handleCloseViewModal = ()=>{
    setFormData({
          id:0,
          name:"",
          description:"",
          price:0
        })
    setViewModalOpen(false)
  }
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };
  // Add/Edit submit
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (editingProduct) {
        await updateProduct(editingProduct.id,formData.name,formData.description,formData.price,selectedCategoryId,selectedFile)
       // await updateCategory(editingCategory.id, categoryName);
        toast.success("Product updated successfully");
        setEditingProduct(null)
        setFormData({
          id:0,
          name:"",
          description:"",
          price:0
        })
        setSelectedCategoryId(null)
        setSelectedFile(null)
      } else {
        //await addCategory(categoryName);
        await addProduct(formData.name,formData.description,formData.price,selectedCategoryId,selectedFile)
        toast.success("Product added successfully");
        setFormData({
          id:0,
          name:"",
          description:"",
          price:0
        })
        setSelectedCategoryId(null)
        setSelectedFile(null)
      }
      fetchProducts();
      handleCloseModal();
    } catch (error) {
      const err = error as AxiosError<{ title: string }>;
      console.log("Error:",error)
      toast.error(err.response?.data?.title || "Something went wrong");
    }
  };

  // Delete category
  const handleView = async(id:number) =>{
    try {
      const response = await getProductById(id)
      setFormData(response.data)
      setViewModalOpen(true)
    } catch (error) {
      const err = error as AxiosError<{title:string}>
      toast.error(err.response?.data?.title || "Failed to retrieve product")
      
    }
  }
  const handleDelete = async (id: number) => {
    try {
      await deleteProduct(id);
      toast.success("Product deleted successfully");
      fetchProducts();
    } catch (error) {
      const err = error as AxiosError<{ title: string }>;
      toast.error(err.response?.data?.title || "Failed to delete product");
    }
  };

  // Columns with inline edit/delete
  const columns: GridColDef[] = [
    { field: "id", headerName: "ID", width: 100 },
    { field: "name", headerName: "Product Name", width: 200 },
      { field: "description", headerName: "Product Description", width: 200 },
        { field: "price", headerName: "Product Price", width: 200 },
        { field: "categoryName", headerName: "Product Category", width: 200 },
    {
      field: "actions",
      headerName: "Actions",
      width: 150,
      sortable: false,
      renderCell: (params: GridRenderCellParams) => (
        <>
         <Tooltip title="View">
            <IconButton onClick={() => handleView(params.row.id)}>
              <VisibilityOutlinedIcon />
            </IconButton>
          </Tooltip>
          <Tooltip title="Edit">
            <IconButton onClick={() => handleOpenModal(params.row)}>
              <EditIcon />
            </IconButton>
          </Tooltip>
          <Tooltip title="Delete">
            <IconButton color="error" onClick={() => handleDelete(params.row.id)}>
              <DeleteIcon />
            </IconButton>
          </Tooltip>
        </>
      ),
    },
  ];

  return (
    <Box m={5}>
      <Box mb={2} display="flex" justifyContent="flex-end">
        <Button variant="contained" onClick={() => handleOpenModal()}>
          Add Product
        </Button>
      </Box>

      <DataGrid
  rows={products}
  columns={columns}
  rowCount={rowCount}
  pagination
  paginationMode="server"
  sortingMode="server"
  pageSizeOptions={[5, 10, 20]}
  getRowId={(row) => row.id}
  autoHeight
  paginationModel={{ page, pageSize }}
  onPaginationModelChange={(model) => {
    setPage(model.page);
    setPageSize(model.pageSize);
  }}
/>
   <Dialog open={viewModalOpen} onClose={handleCloseViewModal} maxWidth="sm" fullWidth>
        <DialogTitle>Product</DialogTitle>
        
          <DialogContent>
            <Box display="flex" flexDirection="column" gap={2}>
    
           <Typography>Product Name: {formData.name}</Typography>
             <Typography>Product Description: {formData.description}</Typography>
               <Typography>Product Price : {formData.price}</Typography>
                <Typography>Category: {formData.categoryName}</Typography>

            </Box>
            
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseViewModal} color="secondary">
              Cancel
            </Button>
           
          </DialogActions>
      
      </Dialog>

      {/* Add/Edit Modal */}
      <Dialog open={modalOpen} onClose={handleCloseModal} maxWidth="sm" fullWidth>
        <DialogTitle>{editingProduct ? "Edit Product" : "Add Product"}</DialogTitle>
        <form onSubmit={handleSubmit}>
          <DialogContent>
            <Box display="flex" flexDirection="column" gap={2}>
      
            <TextField
              label="Product Name"
              value={formData.name}
              name="name"
              onChange={handleChange}
              fullWidth
              required
            />
              <TextField
              label="Product Description"
              value={formData.description}
              name="description"
              onChange={handleChange}
              fullWidth
              required
            />
              <TextField
              label="Product Price"
              value={formData.price}
              name="price"
              onChange={handleChange}
              type="number"
              fullWidth
              required
            />
              <Autocomplete
      disablePortal
      options={categories}
      getOptionLabel={(option)=>option.name}
      isOptionEqualToValue={(option,value) => option.id==value.id}
    value={categories.find((cat)=>cat.id === selectedCategoryId) || null}
    onChange={(event,newValue)=>{
      setSelectedCategoryId(newValue ? newValue?.id : null);
    }}
      renderInput={(params) => <TextField {...params} label="Categories" />}
    />
               <input
        type="file"
        ref={fileInputRef}
        onChange={handleFileChange}
        style={{ display: 'none' }} // Hide the default input
      />
      <Button
        variant="contained"
        startIcon={<CloudUploadIcon />}
        onClick={handleButtonClick}
      >
        Upload Image
      </Button>
            </Box>
            
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseModal} color="secondary">
              Cancel
            </Button>
            <Button type="submit" variant="contained" color="primary">
              {editingProduct ? "Update" : "Add"}
            </Button>
          </DialogActions>
        </form>
      </Dialog>
    </Box>
  );
}
