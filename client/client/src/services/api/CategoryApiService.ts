import  apiClient  from "./ApiClient";

export const getAllCategories = (page:number,pageSize:number)=>{
    return apiClient.api.get(`category/?page=${page}&size=${pageSize}&sortField=id&direction=ASC`)
}

export const addCategory = (name:string)=>{
    return apiClient.api.post(`category/`,{name})
}

export const updateCategory = (id:number,name:string) =>{
    return apiClient.api.put(`category/${id}`,{name})
}

export const deleteCategory = (id:number) => {
    return apiClient.api.delete(`category/${id}`)
}