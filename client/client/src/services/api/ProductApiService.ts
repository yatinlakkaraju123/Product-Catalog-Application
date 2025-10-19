import  apiClient  from "./ApiClient";

export const getAllProducts = (page:number,pageSize:number)=>{
    return apiClient.api.get(`products/?page=${page}&size=${pageSize}&sortField=id&direction=ASC`)
}
export const getAllProductsByCategory = (page:number,pageSize:number,categoryId:number)=>{
    return apiClient.api.get(`products/${categoryId}/?page=${page}&size=${pageSize}&sortField=id&direction=ASC`)
}
export const getProductById = (id:number)=>{
    return apiClient.api.get(`products/${id}`)
}

export const addProduct = (name:string,description:string,price:number,categoryId:number,image:File)=>{
    const data = {name,description,price,categoryId}

    const formData = new FormData()
  const jsonFile = new File(
  [JSON.stringify(data)],
  "data.json",
  { type: "application/json" }
);

formData.append("data", jsonFile);
    if(image!=null && image.size>0) formData.append('image',image)
    
    return apiClient.api.post(`products/`,formData)
}

export const updateProduct = (id:number,name:string,description:string,price:number,categoryId:number,image:File) =>{
   const data = {name,description,price,categoryId}

    const formData = new FormData()
    formData.append(
  'data',
  new Blob([JSON.stringify(data)], { type: 'application/json' })
)
    if(image!=null && image.size>0) formData.append('image',image)
  
    return apiClient.api.put(`products/${id}`,formData,{
        headers:{
            'Content-Type':'multipart/form-data'
        }
    })
}

export const deleteProduct = (id:number) => {
    return apiClient.api.delete(`products/${id}`)
}