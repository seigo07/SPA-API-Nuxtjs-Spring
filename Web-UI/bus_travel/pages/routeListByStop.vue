<template>
  <v-container id="form">
    <v-row>
      <v-col>
        <v-select
          v-model="stop"
          :items="stops"
          label="Stops"
        ></v-select>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-btn :disabled="!this.stop" color="primary" @click="getRoutes()">
          Search
        </v-btn>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-card v-if="routes.length > 0">
          <v-card-title>
            List all routes serving the given stop
            <v-spacer/>
          </v-card-title>
          <v-data-table
            :headers="headers"
            :items="routes"
            :items-per-page="10"
            sort-by="id"
            :sort-desc="true"
            class="elevation-1"
          >
            <template v-slot:item.row="item">
              <tr>
                <td>{{ item.routeId }}</td>
                <td>{{ item.from }}</td>
                <td>{{ item.to }}</td>
                <td>{{ item.totalDurationInMinutes }}</td>
                <td>{{ item.daysOfOperation }}</td>

              </tr>
            </template>
          </v-data-table>
        </v-card>
        <v-card v-if="error !=''">
          <v-card-title>
              {{error}}
            <v-spacer />
          </v-card-title>
        </v-card>
      
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      headers: [
        {text: 'Route Id', value: 'routeId'},
        {text: 'From', value: 'from'},
        {text: 'To', value: 'to'},
        {text: 'Total Duration In Minutes', value: 'totalDurationInMinutes'},
        {text: 'Days Of Operation', value: 'daysOfOperation'},
      ],
      stops: [],
      stop: "",
      dayOfOperation: [],
      routes: [],
      error: ""
    }
  },
  mounted() {
    axios
      .get('/stop/all/names')
      .then((response) => {
        console.log("response.data: ", response.data)
        this.stops = response.data
        // this.daysOfOperation = response.data
      })
      .catch((error) => {
        console.log('There is error:' + error.response)
      })
  },
  methods: {
    async getRoutes() {
      if(this.stop == "") {
        this.error = "Please select a stop";
        return;
      }
      await axios.get(`/route/getroutebystop/${this.stop}`)
      .then((response) => {
        console.log("response.data: ", response.data)
        this.error = response.data.length == 0 ? "No Bus Route found" : ""; 
        this.routes = response.data
      }).catch((error) => {
        console.log('There is error:' + error.response)
      })
    },
  },
}
</script>


<style>
#form{
  width: 50vw;
    text-align: center;
    margin: auto;
    margin-top:10px;
}
</style>