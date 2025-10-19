import  apiClient  from "./ApiClient"
export const executeJWTAuthenticationService = (username:string,password:string)=>
    apiClient.api.post('/auth/v1/login',{
        username,password
    },{withCredentials:true})

    export const registerUser = (username:string,password:string,phoneNumber:string,email:string)=>{
        return apiClient.api.post('/auth/v1/signup',{
            username,password,phoneNumber,email
        },{withCredentials:true})
    }

    export const refreshToken = ()=>{
        return apiClient.api.post('/auth/v1/refreshToken',{
            
        },{withCredentials:true})
    }

    export const logoutUser = ()=>{
        return apiClient.api.post('/auth/v1/logout',{},{withCredentials:true})
    }