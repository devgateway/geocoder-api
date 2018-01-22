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
import org.devgateway.geocoder.repositories.ActivityRepository;
import org.devgateway.geocoder.request.SearchRequest;
import org.devgateway.geocoder.service.ActivityService;
import org.devgateway.geocoder.service.XmlImport;
import org.devgateway.geocoder.web.filterstate.ActivityFilterState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@CrossOrigin
@CacheConfig(keyGenerator = "genericKeyGenerator", cacheNames = "activityController")
public class ActivityController {
    Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    XmlImport xmlImport;

    @Autowired
    ActivityService activityService;

    @Autowired
    ActivityRepository activityRepository;

    @RequestMapping(value = "/import", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public ResponseEntity importXmlFile(@RequestPart("file") final MultipartFile uploadfile,
                                        @RequestPart(name = "autoGeocodeAll", required = false) final String autoGeocodeAll,
                                        @RequestPart(name = "autoGeocodeAllWithoutLoc", required = false) final String autoGeocodeAllWithoutLoc,
                                        @RequestPart(name = "overwriteProjects", required = false) final String overwriteProjects) {
        final Boolean autoGeocode = Boolean.valueOf(autoGeocodeAll);
        final Boolean autoGeocodeWithoutLoc = Boolean.valueOf(autoGeocodeAllWithoutLoc);
        final Boolean overwriteProj = Boolean.valueOf(overwriteProjects);

        try {
            final File file = File.createTempFile(uploadfile.getName(), "xml");
            uploadfile.transferTo(file);

            final Map<Boolean, List<String>> processResults = xmlImport.process(file, uploadfile,
                    autoGeocode, autoGeocodeWithoutLoc, overwriteProj);
            if (processResults.containsKey(Boolean.TRUE) ) {
                return ResponseEntity.status(HttpStatus.OK).body(processResults.get(Boolean.TRUE));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(processResults.get(Boolean.FALSE));
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error while importing file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CollectionUtils.arrayToList(new String[]{"Error when processing this request"}));
        }
    }

    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void exportXmlFile(SearchRequest searchRequest, final HttpServletResponse response) throws IOException {
        final ActivityFilterState activityFilterState = new ActivityFilterState(activityRepository, searchRequest);
        final List<Activity> activities = activityRepository.findAll(activityFilterState.getSpecification());

        final String xml = activityService.generateXML(activities);

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "inline; filename=\"XML Export.xml\"");
        response.setContentLength(xml.length());

        InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }

    @Cacheable
    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    public Page<Activity> getActivityLists(SearchRequest searchRequest) {
        final Pageable pageRequest = new PageRequest(searchRequest.getPage(), 10, Sort.Direction.ASC, "id");
        final ActivityFilterState activityFilterState = new ActivityFilterState(activityRepository, searchRequest);

        return activityRepository.findAll(activityFilterState.getSpecification(), pageRequest);
    }

    @Cacheable
    @RequestMapping(value = "/project/{id}", method = RequestMethod.GET)
    public Activity getActivityById(@PathVariable Long id) {
        return activityRepository.findOne(id);
    }

    @RequestMapping(value = "/project/{id}", method = RequestMethod.PUT)
    public void saveActivity(@PathVariable Long id, @RequestBody Activity activity) {
        activityService.updateActivityLocations(id, activity);
    }


    @RequestMapping(value = "/project/{id}", method = RequestMethod.DELETE)
    public void deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
    }
}
