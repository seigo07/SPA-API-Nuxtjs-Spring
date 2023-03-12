<template>
    <v-app>
        <v-form>
            <v-container>

                <v-row>
                    <v-col>
                        <v-text-field v-model="routeId" :items="routesIdList" label="RouteID"></v-text-field>
                    </v-col>
                </v-row>
                <v-row>
                    <v-col>
                        <v-text-field v-model="from" :items="fromList" label="From"></v-text-field>
                    </v-col>
                </v-row>
                <v-row>
                    <v-col>
                        <v-text-field v-model="to" :items="toList" label="To"></v-text-field>
                    </v-col>
                </v-row>
                <v-row>
                    <v-col>
                        <span>"Enter the timings for each day to add to this route in format HH:MM (24 - Hour format). This
                            field accepts
                            multiple values,seperated by comma."
                        </span>
                    </v-col>
                    <v-col>
                        <v-text-field type="number" label="Enter the number of stops to add to this route"
                            v-model="numberOfStops"></v-text-field>
                    </v-col>
                </v-row>
                <v-row>
                    <v-col id="stopAndTiming">
                        <div v-for="(timing, index) in this.timingPerDay" :num-labels="timingPerDay">
                            <v-label> {{ timing.day }}</v-label>
                            <v-text-field v-model="timing.timing"
                                placeholder="Enter timing for the day. Example - 10:00,11:00">{{
                                    timing.timing }}</v-text-field>
                        </div>
                    </v-col>
                    <v-col id="stopAndTiming">
                        <div v-if="stops.length > 0" v-for="(stop, index) in this.stops">
                            <div id="busStop">
                                <v-label>Stop {{ index + 1 }}</v-label>
                                <v-row>
                                    <v-col>
                                        <v-text-field v-model="stop.busStop" label="Bus stop"></v-text-field>
                                    </v-col>
                                </v-row>
                                <v-row>
                                    <v-col>
                                        <v-text-field v-model="stop.latitude" label="Latitude" type="number"></v-text-field>
                                    </v-col>
                                </v-row>
                                <v-row>
                                    <v-col>
                                        <v-text-field v-model="stop.longitude" label="Longitude"
                                            type="number"></v-text-field>
                                    </v-col>
                                </v-row>
                                <v-row>
                                    <v-col>
                                        <v-select v-model="stop.travelTimeInMinutes" :items="minutes"
                                            label="Travel Time In Minutes">{{
                                                stop.travelTimeInMinutes }}</v-select>
                                    </v-col>
                                </v-row>
                                <v-divider></v-divider>
                            </div>
                        </div>
                    </v-col>
                </v-row>

                <v-row>
                    <v-col>
                        <v-btn color="primary" @click="postNewRoute()">
                            Submit
                        </v-btn>
                    </v-col>
                </v-row>
                <v-card v-if="info != ''">
                    <v-card-title>
                        {{ info }}
                        <v-spacer />
                    </v-card-title>
                </v-card>
            </v-container>
        </v-form>
    </v-app>
</template>
  
<script>
import axios from 'axios';
import { get } from 'https';

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
            numberOfStops: 0,
            stops: [],
            timingPerDay: [{ day: "Monday", timing: '' }, { day: "Tuesday", timing: '' }, { day: "Wednesday", timing: '' }, { day: "Thursday", timing: '' }, { day: "Friday", timing: '' }, { day: "Saturday", timing: '' }, { day: "Sunday", timing: '' }],
            info: "",
        }
    },
    mounted() {
    },

    watch: {
        numberOfStops: function (val) {
            //do something when the data changes.
            if (val) {
                this.changeNumberOfStop(val)
            }
        }
    },

    methods: {
        getBusStation(index) {
            return this.stops.length ? this.stops[index].busStop : ""
        },
        getLatitude(index) {
            return this.stops.length ? this.stops[index].latitude : ""
        },
        getLongitude(index) {
            return this.stops.length ? this.stops[index].longitude : ""
        },
        getTravelTimeInMinutes(index) {
            return this.stops.length ? this.stops[index].travelTimeInMinutes : ""
        },

        addStop() {
            this.stops.push(Stops);
        },

        changeRoute() {
            this.fromList = this.allRoutes.filter(r => r.routeId == this.routeId).map(r => r.from);
            this.toList = this.allRoutes.filter(r => r.routeId == this.routeId).map(r => r.to);
        },

        changeNumberOfStop(numberOfStopsToAdd) {
            try {
                this.stops = [];
                for (let i = 0; i < numberOfStopsToAdd; i++) {
                    this.stops.push({ busStop: '', latitude: '', longitude: '', travelTimeInMinutes: 0 })
                }
            } catch (e) {
                console.log(e)
            }

        },

        async postNewRoute(e) {

            var postObject = {
                "routeId": this.routeId,
                "from": this.from,
                "to": this.to,
                "stops": this.stops.map(stop => ({ name: stop.busStop, coordinates: { latitude: stop.latitude, longitude: stop.longitude }, travelTimeInMinutes: stop.travelTimeInMinutes })),
                "timingsPerDay": this.timingPerDay.map(t => ({ day: t.day, timing: t.timing.split(",") }))
            }
            await axios.post('/route/create', postObject, {
                headers: {
                    'Content-Type': 'application/json',
                },
            }).then((response) => {
                this.info = "Route created successfully."
                console.log("response.data: ", response.data)

                this.allRoutes = []
                this.routeId = ''
                this.fromList = []
                this.toList = []
                this.from = ''
                this.to = ''
                this.travelTimeInMinutes = ''
                this.busStop = ''
                this.latitude = ''
                this.longitude = ''
                this.numberOfStops = 0
                this.stops = []
                this.timingPerDay = [{ day: "Monday", timing: '' }, { day: "Tuesday", timing: '' }, { day: "Wednesday", timing: '' }, { day: "Thursday", timing: '' }, { day: "Friday", timing: '' }, { day: "Saturday", timing: '' }, { day: "Sunday", timing: '' }];

            }).catch((error) => {
                this.info = "Failed to create route.Please check the input and try again."
                console.log('There is error:' + error.response)
            })
        },
    },
};
</script>
  
<style>
#busStop {
    margin: auto;
    text-align: center;
    margin-bottom: 30px;
}

#stopAndTiming {
    margin: 10px;
}
</style>