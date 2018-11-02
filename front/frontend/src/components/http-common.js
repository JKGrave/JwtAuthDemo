import Axios from 'axios'

export const AXIOS = Axios.create({
  baseURL: `/test`,
  headers: {
    'Authorization': 'Bearer ' + localStorage.accessToken
  }
})
