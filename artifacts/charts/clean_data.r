# Remove cancelled runs from dataset

gen_df <- function(in_path, out_path, write_timeouts=FALSE) {
  if (file.exists(in_path)) {
    print(paste("Cleaning file:", in_path))
    df <- read.csv(in_path, sep=",")
    clean_df <- df[(is.na(df$cancelledAt) | df$cancelledAt==""),]
    write.csv(clean_df,paste(out_path,"clean_data.csv",sep="/"), row.names = FALSE)
    if (write_timeouts) {
      # get all rows with "TimeoutException" in any column except the first
      timeout_df <- df[apply(df[,-1], 1, function(x) any(grepl("TimeoutException", x))),]
      # write to timeout.csv in out_path directory
      write.csv(timeout_df,paste(out_path,"timeout.csv",sep="/"), row.names = FALSE)
    }
  } else {
    print(paste("File not found:", in_path))
  }
}

gen_df('data/github/results.csv', 'data/github/')
gen_df('data/kubernetes/results.csv', 'data/kubernetes/')
gen_df('data/snowplow/results.csv', 'data/snowplow/')
gen_df('data/wp/results.csv', 'data/wp/')