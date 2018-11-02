<template>
  <div>
    <h2>Me</h2>
    <div>
      <label>User Info:</label>
      <pre>{{username}}</pre>
    </div>
    <!--
    <div>
      <label>Access Log:</label>
      <div v-for="log in accessLog">{{log.userId}}, {{log.createdAt}}</div>
    </div>
    -->
  </div>
</template>

<script>
  import {AXIOS} from '../http-common'

  export default {
  name: "MyInfo",
  data() {
    return {
      username: null,
      accessLog: null
    }
  },
  created() {
    AXIOS.get('/login/me')
      .then(({data}) => (this.username = data))//, this.accessLog = data.accessLog))
      .catch(() => {
        this.$store.dispatch('LOGOUT').then(()=> this.$router.push('/'))
      })
  }
}
</script>

<style scoped>

</style>
