import { Box, Button, TextField } from '@mui/material'

import type { FormComponentProps } from '../../types/types'
export function FormComponent<T extends Record<string, string>>({
  handleSubmit,
  handleChange,
  formData,
  fields,
}: FormComponentProps<T>) {
  return (
    <div>
      <Box component="form" onSubmit={handleSubmit} display='flex' flexDirection='column' gap={2} sx={{

    p: { xs: 3, md: 10 },
  }} className='card'>
        {fields?.map(({name,label,type})=>(
            <TextField
            name={name}
            label={label}
            type={type}
            onChange={handleChange}
            value={formData.name}
            
            />
        ))}
        <Button type='submit' variant='contained'>Submit</Button>
      </Box>
    </div>
  )
}


