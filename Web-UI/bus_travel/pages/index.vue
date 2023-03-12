<template>
  <v-row justify="center" align="center">    
    <v-col cols="12" sm="8" md="6">
      <v-card class="logo py-4 d-flex justify-center">
        
        <NuxtLogo  id="logo"/>
        <v-card-title class="headline">
          Bus Travel Scheduler
        </v-card-title>
        <VuetifyLogo id="logo" />
      </v-card>   
      <v-card  id="Home">
      </v-card>
    </v-col>
  </v-row>
</template>

<script>
export default {
  name: 'IndexPage'
}
</script>


<style>

#Home{
    background-image: url("static\bus_background.jpeg");
    height : 70vh;
}

#logo {
  width: 100px;
  height: 100px;
  margin: 10px;
}
</style>
