<template>
  <div class="Authorization">
    <h1>Login User</h1>

    <h3>Just some database interaction...</h3>

    <input type="text" v-model="user.username" placeholder="username">
    <input type="password" v-model="user.password" placeholder="password">

    <button @click="loginUser()">Login User</button>

    <div v-if="showResponse"><h6>Api Server returned: {{ response }}</h6></div>

    <!--
    <button v-if="showResponse" @click="retrieveUser()">Retrieve user {{user.id}} data from database</button>

    <h4 v-if="showRetrievedUser">Retrieved User {{retrievedUser.username}} {{retrievedUser.password}}</h4>
    -->
  </div>
</template>

<script>

// import axios from 'axios'
import {AXIOS} from './http-common'

export default {
  name: 'Authorization',
  data () {
    return {
      msg: 'Login Test:',
      user: {
        username: '',
        password: '',
        id: 0
      },
      showResponse: false,
      retrievedUser: {},
      showRetrievedUser: false,
      response: [],
      errors: []
    }
  },
  methods: {
    // Fetches posts when the component is created.
    loginUser () {
      var params = new URLSearchParams()
      params.append('username', this.user.username)
      params.append('password', this.user.password)

      console.log(this.user.username)
      console.log(this.user.password)

      AXIOS.post(`/login`, params)
        .then(response => {
          // JSON responses are automatically parsed.
          this.response = response.data
          this.user.id = response.data
          console.log(response.data)
          this.showResponse = true
        })
        .catch(e => {
          this.errors.push(e)
        })
    },
    retrieveUser () {
      AXIOS.get(`/user/` + this.user.id)
        .then(response => {
          // JSON responses are automatically parsed.
          this.retrievedUser = response.data
          console.log(response.data)
          this.showRetrievedUser = true
        })
        .catch(e => {
          this.errors.push(e)
        })
    }
  }
}

</script>

<style scoped>
  h1, h2 {
    font-weight: normal;
  }
  ul {
    list-style-type: none;
    padding: 0;
  }
  li {
    display: inline-block;
    margin: 0 10px;
  }
  a {
    color: #42b983;
  }

</style>
