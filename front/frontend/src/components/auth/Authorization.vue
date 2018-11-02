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
      let params = new URLSearchParams()
      params.append('username', this.user.username)
      params.append('password', this.user.password)

      /*
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
      */
      // LOGIN 액션 실행
      this.$store.dispatch('LOGIN', params)
        .then(response => {
          console.log('store dispatch returned' + response.data)
          // JSON responses are automatically parsed.
          this.response = response.data
          this.user.id = response.data
          this.showResponse = true
          this.redirect()
        })
        .catch(e => {
          this.errors.push(e)
          // this.response = e.data
          // this.showResponse = true
        })

    },
    // http://blog.jeonghwan.net/2018/03/26/vue-authentication.html
    redirect() {
      const {search} = window.location
      const tokens = search.replace(/^\?/, '').split('&')
      const {returnPath} = tokens.reduce((qs, tkn) => {
        const pair = tkn.split('=')
        qs[pair[0]] = decodeURIComponent(pair[1])
        return qs
      }, {})

      console.log('redirect to: ' + returnPath)
      // 리다이렉트 처리
      this.$router.push(returnPath)
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
