import React from 'react'
import type { ToastComponentProps } from '../../types/types'
import { toast } from 'react-toastify'

const ToastComponent:React.FC<ToastComponentProps> = ({type,message}) => {
 if(type==='success'){
    return(
        toast.success(message,{
            position:'top-right'
 })
    )
}
 if(type==='info'){
    return(
        toast.info(message,{
            position:'top-right'
 })
    )
}
 if(type==='error'){
    return(
        toast.error(message,{
            position:'top-right'
 })
    )
}
 if(type==='warning'){
    return(
        toast.warn(message,{
            position:'top-right'
 })
    )
}

}

export default ToastComponent
