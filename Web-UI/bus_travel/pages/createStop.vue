<template>
  <v-app>
    <v-form v-model="valid" id="form">
      <v-container>
        <v-card v-if="info != ''" id="info">
          <v-card-title>
            {{ info }}
            <v-spacer />
          </v-card-title>
        </v-card>
        <v-row>
          <v-col>
            <v-select v-model="routeId" :items="routesIdList" @change="changeRoute" label="RouteID"></v-select>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-select v-model="from" :items="fromList" label="From"></v-select>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-select v-model="to" :items="toList" label="To"></v-select>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-text-field label="Bus stop" v-model="busStop"></v-text-field>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-text-field label="Latitude" type="number" v-model="latitude"></v-text-field>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-text-field label="Longitude" type="number" v-model="longitude"></v-text-field>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-select v-model="travelTimeInMinutes" :items="minutes" label="Travel Time In Minutes"></v-select>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-btn color="primary"
              :disabled="!this.routeId || !this.from || !this.to || !this.travelTimeInMinutes || !this.busStop"
              @click="postNewStop()">
              Submit
            </v-btn>
          </v-col>
        </v-row>
      </v-container>
    </v-form>
  </v-app>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      routesIdList: [],
      allRoutes: [],
      routeId: '',
      fromList: [],
      toList: [],
      from: '',
      to: '',
      minutes: [...Array(61).keys()],
      travelTimeInMinutes: '',
      busStop: '',
      latitude: '',
      longitude: '',
      info: ''
    }
  },
  mounted() {
    axios
      .get('/stop/all/names')
      // .get('http://localhost:3000/stop/all/names')
      .then((response) => {
        console.log("response.data: ", response.data)
        this.fromList = response.data
        this.toList = response.data
      })
      .catch((error) => {
        console.log('There is error:' + error.response)
      })

    axios
      .get('/route/allrouteid')
      // .get('http://localhost:3000//route/allrouteid')
      .then((response) => {
        console.log("response.data: ", response.data)
        this.routesIdList = response.data
      })
      .catch((error) => {
        console.log('There is error:' + error.response)
      })

    axios
      .get('/route/all')
      // .get('http://localhost:3000//route/all')
      .then((response) => {
        console.log("response.data: ", response.data)
        this.allRoutes = response.data
      })
      .catch((error) => {
        console.log('There is error:' + error.response)
      })
  },

  methods: {
    changeRoute() {
      this.fromList = this.allRoutes.filter(r => r.routeId == this.routeId).map(r => r.from);
      this.toList = this.allRoutes.filter(r => r.routeId == this.routeId).map(r => r.to);
    },



    async postNewStop(e) {
      await axios.put('/route/addstop', {
        "routeId": this.routeId,
        "from": this.from,
        "to": this.to,
        "stop": {
          "name": this.busStop,
          "coordinates": {
            "latitude": this.latitude,
            "longitude": this.longitude,
          },
          "travelTimeInMinutes": this.travelTimeInMinutes,
        },
      }).then((response) => {
        this.info = "Added Stop to route successfully"
        this.routeId = "";
        this.from = "";
        this.to = "";
        this.busStop = "";
        this.latitude = "";
        this.longitude = "";
        this.travelTimeInMinutes = "";
        console.log("response.data: ", response.data)
      }).catch((error) => {
        error.response.data ? this.info = error.response.data :
        "Failed to add stop to route. Please try after some time"
        console.log('There is error:' + error.response)
      })
    }
  },
}

</script>

<style>
#form {
  width: 50vw;
  text-align: center;
  margin: auto;
  margin-top: 10px;
}

#info {
  margin-bottom: 25px;
}
</style>