# Remove cancelled runs from dataset

gen_df <- function(in_path, out_path, write_timeouts=FALSE) {
  if (file.exists(in_path)) {
    print(paste("Cleaning file:", in_path))
    df <- read.csv(in_path, sep=",")
    clean_df <- df[(is.na(df$cancelledAt) | df$cancelledAt==""),]
    write.csv(clean_df,paste(out_path,"clean_data.csv",sep="/"), row.names = FALSE)
    if (write_timeouts) {
      # get all rows with "TimeoutException" in any column except the first
      header <- c("inSize", "objectId")
      timeout_df <- df[apply(df[,-1], 1, function(x) any(grepl("TimeoutException", x))), header]
      # check if timeout.csv already exists
      if (file.exists(paste(out_path, "timeout_excluded.csv", sep="/"))) {
        existing_timeout_df <- read.csv(paste(out_path, "timeout_excluded.csv", sep="/"))
        new_timeout_df <- rbind(existing_timeout_df, timeout_df)
        write.csv(new_timeout_df, paste(out_path, "timeout.csv", sep="/"), row.names = FALSE)
      } else {
        write.csv(timeout_df, paste(out_path, "timeout.csv", sep="/"), row.names = FALSE)
      }
    }
  } else {
    print(paste("File not found:", in_path))
  }
}

gen_df('data/github/results.csv', 'data/github/', write_timeouts=TRUE)
gen_df('data/kubernetes/results.csv', 'data/kubernetes/', write_timeouts=TRUE)
gen_df('data/snowplow/results.csv', 'data/snowplow/', write_timeouts=TRUE)
gen_df('data/wp/results.csv', 'data/wp/', write_timeouts=TRUE)
gen_df('data/allOf_containment/results.csv', 'data/allOf_containment/', write_timeouts=TRUE)
gen_df('data/schemastore_containment/results.csv', 'data/schemastore_containment/', write_timeouts=TRUE)