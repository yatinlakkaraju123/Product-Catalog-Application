import React, { useState, useEffect, useCallback } from "react";
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
} from "@mui/material";
import { DataGrid, type GridColDef, type GridRenderCellParams } from "@mui/x-data-grid";
import { toast } from "react-toastify";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import { AxiosError } from "axios";
import { getAllCategories, addCategory, updateCategory, deleteCategory } from "../../services/api/CategoryApiService";

interface Category {
  id: number;
  name: string;
}

export default function CategoryManager() {
  const [categories, setCategories] = useState<Category[]>([]);
  const [page, setPage] = useState(0);
  const [pageSize, setPageSize] = useState(5);
  const [rowCount, setRowCount] = useState(0);

  const [modalOpen, setModalOpen] = useState(false);
  const [editingCategory, setEditingCategory] = useState<Category | null>(null);
  const [categoryName, setCategoryName] = useState("");

  // Fetch categories from API
  const fetchCategories = useCallback(async () => {
    try {
      const res = await getAllCategories(page, pageSize);
      setCategories(res.data.content);
      setRowCount(res.data.totalElements);
    } catch (error) {
      const err = error as AxiosError<{ title: string }>;
      toast.error(err.response?.data?.title || "Failed to fetch categories");
    }
  }, [page, pageSize]);

  useEffect(() => {
    fetchCategories();
  }, [fetchCategories]);

  // Open modal for add/edit
  const handleOpenModal = (category?: Category) => {
    if (category) {
      setEditingCategory(category);
      setCategoryName(category.name);
    } else {
      setEditingCategory(null);
      setCategoryName("");
    }
    setModalOpen(true);
  };

  const handleCloseModal = () => setModalOpen(false);

  // Add/Edit submit
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (editingCategory) {
        await updateCategory(editingCategory.id, categoryName);
        toast.success("Category updated successfully");
      } else {
        await addCategory(categoryName);
        toast.success("Category added successfully");
      }
      fetchCategories();
      handleCloseModal();
    } catch (error) {
      const err = error as AxiosError<{ title: string }>;
      toast.error(err.response?.data?.title || "Something went wrong");
    }
  };

  // Delete category
  const handleDelete = async (id: number) => {
    try {
      await deleteCategory(id);
      toast.success("Category deleted successfully");
      fetchCategories();
    } catch (error) {
      const err = error as AxiosError<{ title: string }>;
      toast.error(err.response?.data?.title || "Failed to delete category");
    }
  };

  // Columns with inline edit/delete
  const columns: GridColDef[] = [
    { field: "id", headerName: "ID", width: 100 },
    { field: "name", headerName: "Name", width: 200 },
    {
      field: "actions",
      headerName: "Actions",
      width: 150,
      sortable: false,
      renderCell: (params: GridRenderCellParams) => (
        <>
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
          Add Category
        </Button>
      </Box>

      <DataGrid
  rows={categories}
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


      {/* Add/Edit Modal */}
      <Dialog open={modalOpen} onClose={handleCloseModal} maxWidth="sm" fullWidth>
        <DialogTitle>{editingCategory ? "Edit Category" : "Add Category"}</DialogTitle>
        <form onSubmit={handleSubmit}>
          <DialogContent>
            <TextField
              label="Category Name"
              value={categoryName}
              onChange={(e) => setCategoryName(e.target.value)}
              fullWidth
              required
            />
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseModal} color="secondary">
              Cancel
            </Button>
            <Button type="submit" variant="contained" color="primary">
              {editingCategory ? "Update" : "Add"}
            </Button>
          </DialogActions>
        </form>
      </Dialog>
    </Box>
  );
}
