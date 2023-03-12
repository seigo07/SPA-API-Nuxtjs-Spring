<template>
  <v-container id="form">
    <v-row>
      <v-col>
        <v-select v-model="stop" :items="stops" label="Stops"></v-select>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-select v-model="day" :items="days" label="Days Of Operation"></v-select>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <dashboard-input-label style=" :#b8b8b8" identifier="start_time">
          Time of travel
        </dashboard-input-label>
      
          <input 
                                        
                                        type="time"
                                        class="form-input w-full relative z-10"
                                        placeholder="Start time"
                                        v-model="time" id="time" name="start_time"
                                    >
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-btn color="primary" :disabled="!this.stop" @click="getRoutes()">
          Display routes
        </v-btn>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-card v-if="routes.length > 0">
          <v-card-title>
            Listing all routes serving a given stop, day/time
            <v-spacer />
          </v-card-title>
          <v-data-table :headers="headers" :items="routes" :items-per-page="10" sort-by="id" :sort-desc="true"
            class="elevation-1">
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
            {{ error }}
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
        { text: 'Route Id', value: 'routeId' },
        { text: 'From', value: 'from' },
        { text: 'To', value: 'to' },
        { text: 'Total Duration In Minutes', value: 'totalDurationInMinutes' },
        { text: 'Days Of Operation', value: 'daysOfOperation' },
      ],
      stops: [],
      stop: "",
      days: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"],
      day: "",
      dayOfOperation: [],
      routes: [],
      error: "",
    }
  },
  mounted() {
    axios
      .get('/stop/all/names')
      .then((response) => {
        this.stops = response.data
      })
      .catch((error) => {
        console.log('There is error:' + error.response)
      })
  },
  methods: {
    async getRoutes() {
      if (!this.stop) {
        this.error = "Please select a stop";
        return;
      }
      await axios.get(`/route/getroute?stop=${this.stop}${this.day ? '&day=' + this.day : ""}${this.time ? '&time=' + this.time : ''}`)
        .then((response) => {
          this.error = response.data ? "" : "No Bus Route found for the given stop, day/time";
          this.routes = response.data
        }).catch((error) => {
          console.log('There is error:' + error.response)
        })
    },
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

#time {
  color: white;
  border: 1px solid white;
  border-radius: 5px;
  margin-left: 10px;
  background-color: brown;
  width:150px;
  padding:10px;
}</style>