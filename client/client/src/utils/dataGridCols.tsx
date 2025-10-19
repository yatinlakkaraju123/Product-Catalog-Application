import type { GridColDef, GridRenderCellParams } from "@mui/x-data-grid";
import type { categoryRow } from "../types/types";
import { IconButton, Tooltip } from "@mui/material";
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import DeleteOutlineOutlinedIcon from '@mui/icons-material/DeleteOutlineOutlined';
export const categoryColumns = (
onEdit: (id:number) => void,
onDelete: (id:number) => void

): GridColDef<(categoryRow[])[number]>[] => [
      {
    field: 'id',
    headerName: 'ID',
     type: 'number',
    width: 40,
    editable: true,
  },
  {
    field: 'name',
    headerName: 'Category Name',
   
    width: 160,
    editable: true,
  },
   {
      field: "actions",
      headerName: "Actions",
      sortable:false,
      minWidth: 100,
      renderCell: (params: GridRenderCellParams) => (
        <div>
          <Tooltip title="Edit">
        <IconButton onClick={()=>onEdit(params.row.id)}>
          <EditOutlinedIcon/>
        </IconButton>
          </Tooltip>
           <Tooltip title="Delete">
        <IconButton onClick={()=>onDelete(params.row.id)}>
          <DeleteOutlineOutlinedIcon/>
        </IconButton>
          </Tooltip>
        </div>
      )
    },
]