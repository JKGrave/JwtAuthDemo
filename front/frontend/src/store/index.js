import Vue from 'vue';
import Vuex from 'vuex';
import {AXIOS} from '../components/http-common';

Vue.use(Vuex);

// http://blog.jeonghwan.net/2018/03/26/vue-authentication.html

const enhanceAccessToken = () => {
  const {accessToken} = localStorage
  console.log('enhanceAccessToken > accessToken: ' + accessToken)
  if (!accessToken) {
    console.log('no access token')
    return
  }
  AXIOS.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`
}
enhanceAccessToken()

export default new Vuex.Store({
  state: {
    accessToken: null
  },
  getters: {
    isAuthenticated (state) {
      state.accessToken = state.accessToken || localStorage.accessToken
      return state.accessToken
    }
  },
  mutations: {
    LOGIN (state, accessToken) {
      state.accessToken = accessToken

      // 토큰을 로컬 스토리지에 저장
      localStorage.accessToken = accessToken

      console.log('token stored: ' + localStorage.accessToken)
    },
    LOGOUT (state) {
      state.accessToken = null
      delete localStorage.accessToken

      console.log('token deleted: ' + localStorage.accessToken)
    }
  },
  actions: {
    LOGIN ({commit}, params) {
      return AXIOS.post(`/login`, params)
        .then(({data}) => {

            // AXIOS.default.headers.common['Authorization'] = `Bearer ${data}`;
            commit('LOGIN', data)

            console.log('after header')
            this.$router.push({name: 'HelloWorld'})
        })
    },
    LOGOUT ({commit}) {
      AXIOS.defaults.headers.common['Authorization'] = undefined
      commit('LOGOUT')
    },
  }
})
