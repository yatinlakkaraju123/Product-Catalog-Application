import type { GridColDef } from "@mui/x-data-grid";

export type loginPage = Record<"username" | "password", string>;
export type signUpPage = Record<"username" | "password"|"phoneNumber"|"email", string>;
export type addCategoryPage = Record<"name",string>;
export interface FormComponentProps<T extends Record<string, string>> {
  handleSubmit: (e: React.FormEvent) => void;
  handleChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  formData: T;
  fields: FieldConfig[];
}

export interface FieldConfig {
  name: string;
  label: string;
  type?: string;
}

export interface ToastComponentProps{
  type:"success"|"error"|"warning"|"info";
  message:string;
}

export type categoryRow = {
  id:number,
  name:string;
}
export type productRow = {
  id:number,
  name:string;
}

export interface DataGridProps{
  columns:GridColDef[]
  rows:categoryRow[] | productRow[]
  page:number
  pageSize:number 
  handlePageChange: (param:number)=>void
  handlePageSizeChange: (param:number)=>void
  dataGridWidth:string
  rowCount:number

}

export interface ModalComponentProps<T extends Record<string, string>>{
  open:boolean
  handleClose: ()=>void
  success: ()=>void
  formData: T
  setFormData: (param:any) => void
}
export interface ProductComponent{
  name:string;
  description:string;
  imageUrl?:string;
  price:number;
  categoryName:string;
  key:number
}
// export type customError = {

// }