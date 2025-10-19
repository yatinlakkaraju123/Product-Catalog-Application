import React from "react";
import { AxiosError } from "axios";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
} from "@mui/material";
import type {  ModalComponentProps } from "../../../types/types";
import { FormComponent } from "../../common/FormComponent";
import { addCategory } from "../../../services/api/CategoryApiService";
import { toast } from "react-toastify";

function AddCategoryDialog<T extends Record<string, string>>(
  { open, handleClose, success, formData,setFormData }: ModalComponentProps<T>
): React.ReactElement {

 const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
     console.log(formData);
     try {
      await addCategory(formData.name)
      toast.success('Category added successfully')
      success()
      setFormData({
        name:""
      })
      handleClose()
     } catch (error) {
      const err = error as AxiosError<{title:string}>
      toast.error(err.response?.data?.title ?? "Something went wrong")
     }
    }
  return (
    <Dialog open={open} onClose={handleClose} maxWidth="sm" fullWidth>
      <DialogTitle>Add Category</DialogTitle>

      <DialogContent>
        <FormComponent
          formData={formData}
          handleChange={handleChange}
          handleSubmit={handleSubmit}
          fields={[
            { name: "name", label: "Category Name", type: "text" },
          ]}
        />
      </DialogContent>

      <DialogActions>
        <Button onClick={handleClose} color="secondary">
          Cancel
        </Button>
        
      </DialogActions>
    </Dialog>
  );
};

export default AddCategoryDialog;
