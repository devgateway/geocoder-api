/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.devgateway.geocoder.web;

import org.devgateway.geocoder.domain.Activity;
import org.devgateway.geocoder.iati.model.IatiActivity;
import org.devgateway.geocoder.repositories.ActivityRepository;
import org.devgateway.geocoder.service.ActivityService;
import org.devgateway.geocoder.service.XmlImport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@CrossOrigin
public class ActivityController {
    Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    XmlImport xmlImport;

    @Autowired
    ActivityService activityService;

    @Autowired
    ActivityRepository activityServicRepository;

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public ResponseEntity importXmlFile(@RequestParam("file") MultipartFile uploadfile) {
        log.info(uploadfile.getName());
        try {
            xmlImport.process(uploadfile.getInputStream(), "en");
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error while imporing file");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while importing file");
        }
    }

    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    public List<Activity> getActivityLists(@RequestParam(value = "", required = false) String t, @RequestParam(required = false) Integer page) {

        //return activityServicRepository.findByText("%Solar%");
        try {
            return activityServicRepository.findByDate(
                    new SimpleDateFormat("YYYY-MM-DD").parse("2004-01-01"),
                    new SimpleDateFormat("YYYY-MM-DD").parse("2010-01-01")
            );
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    @RequestMapping(value = "/project/{id}", method = RequestMethod.GET)
    public Activity getActivityById(@PathVariable Long id) {
        return activityService.getActivityById(id);
    }

}
