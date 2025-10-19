import * as React from 'react';
import Box from '@mui/material/Box';
import { DataGrid } from '@mui/x-data-grid';
import type { DataGridProps } from '../../types/types';



export default function DataGridDemo({rows,columns,page,pageSize,handlePageChange,handlePageSizeChange,dataGridWidth,rowCount}:DataGridProps):React.JSX.Element {

  return (
    <Box sx={{ height: 400, width: dataGridWidth }}>
      <DataGrid
        rows={rows}
        columns={columns}
        paginationMode='server'
        sortingMode='server'
        rowCount={rowCount}
        pagination
        paginationModel={{page,pageSize}}
        onPaginationModelChange={(model) => {
    handlePageChange(model.page);
    handlePageSizeChange(model.pageSize);

  }}
  pageSizeOptions={[1,5,10]}
getRowId={(row)=>row.id}      
      />
    </Box>
  );
}
